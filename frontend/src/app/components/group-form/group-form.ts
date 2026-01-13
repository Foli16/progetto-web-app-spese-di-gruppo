import { ChangeDetectionStrategy, Component } from '@angular/core';
import { SpendingGroupPost } from '../../../model/SpendingGroupPost';
import { GroupService } from '../../../services/GroupService';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-group-form',
  imports: [FormsModule],
  templateUrl: './group-form.html',
  styleUrl: './group-form.css'
})
export class GroupForm { 
  groupPost:SpendingGroupPost = {name:"", participants:[{name:"", founder: true}]};

  constructor(public serv:GroupService)
  {  }

  addParticipant()
  {
    if(this.fillCheck())
      this.groupPost.participants.push({name:"", founder:false});
  }

  removeParticipant(index:number)
  {
    this.groupPost.participants.splice(index, 1);
  }

  fillCheck()
  {
    return this.groupPost.participants.every(p => p.name && p.name.trim().length > 0);
  }

  save()
  {
    this.serv.createGroup(this.groupPost);
    this.groupPost = {name:"", participants:[{name:"", founder: true}]};
  }
}
