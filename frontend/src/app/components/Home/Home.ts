import { Component } from '@angular/core';
import { GroupService } from '../../../services/GroupService';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './Home.html',
  styleUrl: './Home.css',
})
export class Home {
    constructor(public serv:GroupService)
    {
      this.fillArray();
    }

    fillArray()
    {
      this.serv.getGroupList();
    }
 }
