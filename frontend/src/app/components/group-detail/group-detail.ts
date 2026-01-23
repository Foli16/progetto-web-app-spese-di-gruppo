import { Component, Input } from '@angular/core';
import { GroupService } from '../../../services/GroupService';
import { GroupPreviewGet } from '../../../model/GroupPreviewGet';
import { GroupDetailGet } from '../../../model/GroupDetailGet';

@Component({
  selector: 'app-group-detail',
  imports: [],
  templateUrl: './group-detail.html',
  styleUrl: './group-detail.css',
})
export class GroupDetail {
  
  group:GroupDetailGet | null = null;
  basicInfo:GroupPreviewGet | null = null;
  
  constructor(public serv:GroupService)
  {
    this.fillGroup();
  }
  
  fillGroup()
  {
    this.group = this.serv.openedGroup;
    this.basicInfo = this.serv.openedGroupBasicInfo;
  }
 }
