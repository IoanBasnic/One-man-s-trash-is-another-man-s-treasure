import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-myprofile',
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

  constructor() { }

  ngOnInit(): void {
  }

}
