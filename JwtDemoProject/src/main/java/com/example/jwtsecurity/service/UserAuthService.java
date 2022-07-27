package com.example.jwtsecurity.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import static org.springframework.security.core.userdetails.User.withUsername;
import com.example.jwtsecurity.config.JwtProvider;
import com.example.jwtsecurity.models.Role;
import com.example.jwtsecurity.models.User;
import com.example.jwtsecurity.repo.RoleRepo;
import com.example.jwtsecurity.repo.UserRepo;

@Service
@Transactional
public class UserAuthService implements UserDetailsService {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private AuthenticationManager authmanager;
	@Autowired
	private PasswordEncoder pencoder;
	@Autowired
	private RoleRepo rRepo;
	@Autowired
	private JwtProvider jprovider;

	public User loadUserByUserID(Integer id) {
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent())
			return user.get();
		else
			throw new UsernameNotFoundException("User ID not found");
	}

	@Override
	public User loadUserByUsername(String username) {
		Optional<User> user = userRepo.findByUsername(username);
		if (user.isPresent())
			return user.get();
		else
			throw new UsernameNotFoundException("User ID not found");
	}

	public List<User> findAllUser() {
		return userRepo.findAll();
	}

	public List<Role> findAllRole() {
		return rRepo.findAll();
	}

	public String deleteUserById(Integer id) throws UsernameNotFoundException {
		Optional<User> u = userRepo.findById(id);
		if (u.isPresent()) {
			userRepo.deleteById(id);
			return "User with id " + id + " and name " + u.get().getUsername() + " deleted successfully!!";
		} else
			return "User with id " + id + " not found";
	}
	public String deleteRoleById(Integer id) throws UsernameNotFoundException {
		Optional<Role> role = rRepo.findById(id);
		if (role.isPresent()) {
			rRepo.deleteById(id);
			return "User with id " + id + " and name " + role.get().getRoleName() + " deleted successfully!!";
		} else
			return "User with id " + id + " not found";
	}

	public Role saveRole(Role role) {
		Optional<Role> roles = rRepo.findByRoleName(role.getRoleName());
		if (roles.isPresent()) {
			if (role.getRoleId() == null)
				role.setRoleId(roles.get().getRoleId());
			if ( role.getDescription()==null || role.getDescription().isBlank())
				role.setDescription(roles.get().getDescription());
			return rRepo.saveAndFlush(role);
		} else {
			return rRepo.saveAndFlush(role);
		}
	}
	public Role findRoleByRoleName(String name) {
		Optional<Role> role = rRepo.findByRoleName(name);
		if (role.isPresent())
			return role.get();
		else
			return new Role();
	}

	public Authentication login(String username, String password) {
		return authmanager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}

	public Map<String, String> signin(String username, String password, HttpServletResponse res)
			throws UsernameNotFoundException, HttpServerErrorException {
		// Optional<String> token=Optional.empty();
		Map<String, String> m1 = new HashMap();
		Optional<User> user = userRepo.findByUsername(username);
		if (user.isPresent()) {
			try {
				authmanager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
				// token=Optional.of(jprovider.generateToken(username));
				m1 = jprovider.generateToken(username, res);
				// token=Optional.of(jprovider.generateToken(username,user.get().getRoles());
			} catch (AuthenticationException e) {
				// TODO: handle exception
			}
		}
		return m1;
	}

	public Optional<User> signup(String username, String password) throws UsernameNotFoundException {
		if (!userRepo.findByUsername(username).isPresent()) {
			/*
			 * System.out.println("User Details By user service - "+password+" -- "+username
			 * ); if(!rRepo.findByRoleName("USER").isPresent()) {
			 * System.out.println("Role will be added !"); rRepo.saveAndFlush(new
			 * Role("USER","This is temporary role with limited access")); }
			 */
			Set<Role> s1 = new HashSet<Role>();
			s1.add(rRepo.findByRoleName("USER").get());
			return Optional.of(userRepo.save(new User(username, pencoder.encode(password), s1)));
		}
		return Optional.empty();
	}

	public Optional<User> adminsignup(String username, String password, Set<Role> s1) throws UsernameNotFoundException {
		Optional<User> user = userRepo.findByUsername(username);
		if (!user.isPresent()) {
			/*
			 * System.out.println("User Details By admin - "+s1.toString()+" -- "+username);
			 * //Set<Role> s1= new HashSet<Role>(); //s1.add(new Role("USER")); //Set<Role>
			 * s2= new HashSet<Role>(); for(Role x:s1)
			 * if(!rRepo.findByRoleName(x.getRoleName()).isPresent()) {
			 * rRepo.saveAndFlush(new Role(x.getRoleName(),x.getDescription())); }
			 */
			Set<Role> s2 = new HashSet<Role>();
			s1.forEach(role -> s2.add(rRepo.findByRoleName(role.getRoleName()).get()));
			System.out.println("from signup admin "+s2.toString());
			return Optional.of(userRepo.save(new User(username, pencoder.encode(password), s2)));
		}
		/*
		 else { User u=user.get(); u.getRoles().addAll(s1); return
		  Optional.of(userRepo.save(u)); }
		 */
		return Optional.empty();
	}

	public Optional<UserDetails> loadUserByJwtToken(String jwtToken) throws UsernameNotFoundException {
		if (jprovider.validateToken(jwtToken)) {
			User u = jprovider.getUser(jwtToken);
			List<GrantedAuthority> authorities = new ArrayList();
			for (Role role : u.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
			}
			return Optional.of(withUsername(u.getUsername()).authorities(authorities).password("") // token does not
																									// have password but
																									// field may not be
																									// empty
					.accountExpired(false).accountLocked(false).credentialsExpired(false).disabled(false).build());
		}
		return Optional.empty();
	}
	
	public Optional<User> loadUserByToken(String token) {
		//System.out.println("test 1 "+token);
		
		if (token != null && token.startsWith("Bearer")) {
            token = token.replace("Bearer ", "").trim();
           // System.out.println("tst2 "+token);
            if(jprovider.validateToken(token)) return Optional.of(jprovider.getUser(token));
        }
		
		
		
        return Optional.empty();
		
		
		

	}

}