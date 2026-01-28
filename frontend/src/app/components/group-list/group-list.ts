import { Component } from '@angular/core';
import { GroupService } from '../../../services/GroupService';
import { GroupPreviewGet } from '../../../model/GroupPreviewGet';
import { Router } from '@angular/router';

@Component({
  selector: 'app-group-list',
  imports: [],
  templateUrl: './group-list.html',
  styleUrl: './group-list.css',
})
export class GroupList {
    constructor(public serv:GroupService, public router:Router)
    {
      this.fillArray();
    }

    fillArray()
    {
      this.serv.getGroupList();
    }

    openGroup(groupId:string, myParticipantId:string)
    {
      this.router.navigate(["/group-detail", groupId, myParticipantId]);
    }
 }
