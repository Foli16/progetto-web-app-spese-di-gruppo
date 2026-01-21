import { Component } from '@angular/core';
import { GroupService } from '../../../services/GroupService';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './group-list.html',
  styleUrl: './group-list.css',
})
export class GroupList {
    constructor(public serv:GroupService)
    {
      this.fillArray();
    }

    fillArray()
    {
      this.serv.getGroupList();
    }
 }
