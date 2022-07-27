import { Component, OnInit } from '@angular/core';
import { JwtClientService } from 'src/app/jwt-client.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  constructor(private jservice:JwtClientService) { }
   public isLoggedIn=false;

  ngOnInit(): void {
    this.isLoggedIn=this.jservice.isLoggedIn();
  }
  logoutUser(){
    this.jservice.logout();
    location.reload();
  }

}
