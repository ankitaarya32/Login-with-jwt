import { Component, OnInit } from '@angular/core';
import { JwtClientService } from '../jwt-client.service';

@Component({
  selector: 'app-security',
  templateUrl: './security.component.html',
  styleUrls: ['./security.component.css']
})
export class SecurityComponent implements OnInit {

  authRequest:any={
    "username":"Ankit",
    "password":"test"
  };
  response : any;
  constructor(private service:JwtClientService) { }

  ngOnInit(): void {
    this.getAccessToken(this.authRequest);
  }

  public getAccessToken(authRequest){
    let resp = this.service.generateToken(authRequest);
    let token='';
    resp.subscribe(data=>{
      let dx=JSON.stringify(data);
      let df=JSON.parse(dx);
      token=df.token;
      console.log("token "+token);
    });

    resp.subscribe(data => this.accessAPI(token));
  }

  public accessAPI(token){
    let resp=this.service.AdminTest(token);
    resp.subscribe(data=>this.response=data);
  }

}
