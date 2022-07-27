import { Component, OnInit } from '@angular/core';
import { JwtClientService } from 'src/app/jwt-client.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private jservice:JwtClientService ) { }

  credentials = {
    username : "",
    password: ""
  }

  ngOnInit(): void {
  }

  onSubmit(){
    if((this.credentials.username!=null && this.credentials.password!=null)&&(this.credentials.username.trim()!='' && this.credentials.password.trim()!='') ){
      this.jservice.generateLoginToken(this.credentials).subscribe(
       (response:any) =>{
          console.log(response+" -- "+response.token);
          if(response==null||response.token==null ||response.token=='' ) console.log("Invalid Credential "+JSON.stringify(response));
          else{
            this.jservice.loginUser(response.token);
            window.location.href="/dashboard";
          }

        },
        error =>{
          console.log(error);

        }
      );
      
      console.log(JSON.stringify(this.credentials));
    }
    else
    console.log(JSON.stringify("fields are empty"));
  }

}
