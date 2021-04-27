import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpClientModule} from '@angular/common/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    const header = document.querySelector('nav');
    const sectionOne = document.querySelector('.wrapper');

    header.classList.add('nav-noscroll');
    header.classList.remove('.navigation');

  }

  sendData(data): void {
    this.http.post<any>('https://reqres.in/api/posts', { title: 'Angular POST Request Example' }).subscribe(data);
  }

}
