import { Component } from '@angular/core';
import { GroupService } from '../../../services/GroupService';
import { GroupPreviewGet } from '../../../model/GroupPreviewGet';

@Component({
  selector: 'app-group-list',
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

    openGroup(group:GroupPreviewGet)
    {
      this.serv.getGroupDetail(group);
    }
 }
