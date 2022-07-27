import { Component, OnInit } from '@angular/core';
import { JwtClientService } from 'src/app/jwt-client.service';

@Component({
  selector: 'app-userdetail',
  templateUrl: './userdetail.component.html',
  styleUrls: ['./userdetail.component.css']
})
export class UserdetailComponent implements OnInit {

  constructor(private jwtservice:JwtClientService) { }

  users={
    username:"",
    password:"",
    roles:[{
      roleId:"",
      roleName:"",
      description:""
    }]
    
  }

  
  ngOnInit(): void {
    this.jwtservice.getUserByToken().subscribe(
      (response:any)=>{if(response!=null){
        console.log("this ->1 "+response)
        this.users.username=response.username;
        this.users.password=response.password;
        this.users.roles=response.roles;
    
      }
    else console.log("this ->2 "+response)
    },
    error=>console.log("this ->3 "+error));
  }

  onSubmit(){

  }

}
