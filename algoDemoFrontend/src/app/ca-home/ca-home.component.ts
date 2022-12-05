import { Component } from '@angular/core';

@Component({
  selector: 'app-ca-home',
  templateUrl: './ca-home.component.html',
  styleUrls: ['./ca-home.component.css']
})
export class CaHomeComponent {

  becomered(num:string):void{
    const box = document.getElementById(num);
      box?.classList.add('list-group-item-danger');
  }
}
