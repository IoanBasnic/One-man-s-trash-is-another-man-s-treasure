import { Component, OnInit } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import {AgmCoreModule} from '@agm/core';
import {HttpClient} from '@angular/common/http';
import {GlobalConstants} from '../../common/global-constants';
@Component({
  selector: 'app-user-profile',
  templateUrl: './myprofile.component.html',
  styleUrls: ['./myprofile.component.css']
})
export class MyprofileComponent implements OnInit {
  client = {
    name: '<add name>',
    email: ' <add email>',
    address: '<add address>',
    phoneNumber: '<add phone number>'
  };
  lat = 44.426164962;
  lng = 26.102332924;

  constructor(public auth: AuthService, private http: HttpClient) { }

  // @ts-ignore
  ngOnInit(): void {
    const header = document.querySelector('nav');
    const sectionOne = document.querySelector('.wrapper');

    header.classList.add('nav-noscroll');
    header.classList.remove('.navigation');
  }

  selectLocation($event): void {
    console.log(event);
  }
}
