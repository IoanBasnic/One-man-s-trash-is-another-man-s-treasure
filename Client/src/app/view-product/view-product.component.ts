import { Component, OnInit } from '@angular/core';
import {GlobalConstants} from '../../common/global-constants';
import {AppModule} from '../app.module';
import {FormBuilder} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {AuthService} from '@auth0/auth0-angular';

@Component({
  selector: 'app-view-product',
  templateUrl: './view-product.component.html',
  styleUrls: ['./view-product.component.css']
})
export class ViewProductComponent implements OnInit {
  ProductID: any;
  url = GlobalConstants.apiURL + 'product';
  itemList;
  product = {
    title: '<add title>',
    description: ' <add desc>',
    price: '<add price>',
  };
  client = {
    name: '<add name>',
    description: ' <add desc>',
    price: '<add price>',
  };
  constructor(private fb: FormBuilder, private http: HttpClient, public auth: AuthService) { }

  ngOnInit(): void {
    this.ProductID = localStorage.getItem('productID');
    this.http.get(this.url, {responseType: 'json'}).subscribe(responseData => {
      this.itemList = responseData;
      this.createContent();
    });
  }

  createContent(): void {
    this.itemList.forEach((item) => {
      console.log(item.price);
      if (item.id === this.ProductID) {
        this.product.title = item.title;
        this.product.description = item.description;
        this.product.price = item.askingPrice;
      }
    });
  }
}
