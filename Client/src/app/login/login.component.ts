import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  myForm: FormGroup;

  postData = {
  };
  url = 'http://localhost:8080/loginClient';

  constructor(private fb: FormBuilder, private http: HttpClient) {
    // this.http.post(this.url, this.postData).toPromise().then(data => {console.log(data); });
  }

  ngOnInit(): void {
    const header = document.querySelector('nav');
    const sectionOne = document.querySelector('.wrapper');

    header.classList.add('nav-noscroll');
    header.classList.remove('.navigation');

    this.myForm = this.fb.group({username: '', password: ''});
    this.myForm.valueChanges.subscribe(console.log);
  }

  loginClient(): void {
    this.postData = {
      username: this.myForm.getRawValue().username,
      email: this.myForm.getRawValue().email,
      password: this.myForm.getRawValue().password
    };
    // this.http.post(this.url, 200, {observe: 'response'}).subscribe(resp => {
     //   console.log(resp);
     // });
    this.http.post(this.url, this.postData, {observe: 'response'}).subscribe(resp => {
      if (resp.status === 200) {
        window.location.href = '/profile';
      }
      else {
        alert('Error: ' + resp.status);
      }
     });
  }
}
