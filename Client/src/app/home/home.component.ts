import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
    const header = document.querySelector('nav');
    const items = document.querySelector('.itemsList_a');
    const sectionOne = document.querySelector('.wrapper');

    const sectionOneOptions = {};
    header.classList.remove('nav-noscroll');
    header.classList.add('.navigation');

    if (sectionOne == null)
    {
      header.classList.add('nav-noscroll');
      header.classList.remove('.navigation');
    };
    const sectionOneObserver = new IntersectionObserver(function(entries, sectionOneObserver) {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          header.classList.add('nav-scroll');
        }
        else {
          header.classList.remove('nav-scroll');
        }
      });
    }, sectionOneOptions);

    sectionOneObserver.observe(sectionOne);

  }

}
