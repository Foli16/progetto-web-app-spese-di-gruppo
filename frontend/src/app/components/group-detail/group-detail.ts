import { Component, Input } from '@angular/core';
import { GroupService } from '../../../services/GroupService';
import { GroupPreviewGet } from '../../../model/GroupPreviewGet';
import { GroupDetailGet } from '../../../model/GroupDetailGet';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-group-detail',
  imports: [],
  templateUrl: './group-detail.html',
  styleUrl: './group-detail.css',
})
export class GroupDetail {
  
  // group:GroupDetailGet | null = null;
  // basicInfo:GroupPreviewGet | null = null;
  
  constructor(public serv:GroupService, private route:ActivatedRoute)
  {
    this.getGroupDetail();
  }
  
  getGroupDetail()
  {
    const id = this.route.snapshot.paramMap.get("id");
    const partId = this.route.snapshot.paramMap.get("partId");
    this.serv.getGroupDetail(id, partId);
  }
 }
