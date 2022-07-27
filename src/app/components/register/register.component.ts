import { Component, OnInit } from '@angular/core';
import { JwtClientService } from 'src/app/jwt-client.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor(private jservice:JwtClientService ) { }

  credentials = {
    username : "",
    password: ""
  }
  successmsg=""

  ngOnInit(): void {
    this.successmsg=""
  }

  onSubmit(){
    if((this.credentials.username!=null && this.credentials.password!=null)&&(this.credentials.username.trim()!='' && this.credentials.password.trim()!='') ){
      this.jservice.registerUser(this.credentials).subscribe(
       (response:any) =>{
          
          if(response==null ||response == '' ) {
            this.successmsg="User is already registered. To reset password click";
          }
          else{
            this.successmsg = "User registered successful with id : "+response.userId;
            
          }

        },
        error =>{
          console.log(error);

        }
      );
      
      console.log(JSON.stringify(this.credentials));
    }
    else{
      this.successmsg="Either username or password is not appropriate";
    console.log(JSON.stringify("fields are empty"));
    }
  }

}