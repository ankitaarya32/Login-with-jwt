import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class JwtClientService {
   baseUrl="http://localhost:8080";

  constructor(private http:HttpClient) { }

  public generateToken(request){
    return this.http.post(this.baseUrl+'/api/public/signin',request,{responseType : 'json'});
  }
//'Text' as 'json'
  public AdminTest(token){
    console.log("from client : "+token);
    const headers= new HttpHeaders().set("Authorization",token);
    return this.http.get(this.baseUrl+'/api/admin/test',{headers,responseType :'Text' as 'json' })
  }

  loginUser(token){
    sessionStorage.setItem("token",token)
  }
  isLoggedIn(){
    let token=sessionStorage.getItem("token");
    if(token==undefined || token ==''|| token==null) return false;
    else return true;
  }
  logout(){
    sessionStorage.removeItem('token');
    return true;
  }

  public getToken(){
    return sessionStorage.getItem('token');
  }

  public generateLoginToken(credentials: any){
    return this.http.post(this.baseUrl+"/api/public/signin",credentials);
  }

  public getUserByToken(){
    const token = this.getToken();
    return this.http.post(this.baseUrl+"/api/public/get-user",token);
  }
  public registerUser(credentials: any){
    return this.http.post(this.baseUrl+"/api/public/signup",credentials);
  }
}
