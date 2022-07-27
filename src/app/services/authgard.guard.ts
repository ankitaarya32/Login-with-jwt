import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { JwtClientService } from '../jwt-client.service';

@Injectable({
  providedIn: 'root'
})
export class AuthgardGuard implements CanActivate {
  constructor(private jservice:JwtClientService, private router:Router){}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        console.log(this.jservice.isLoggedIn());
      if(this.jservice.isLoggedIn())
            {return true;}

    this.router.navigate(["login"]);
    return false;
  }
  
}
