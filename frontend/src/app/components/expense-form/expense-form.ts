import { Component } from '@angular/core';
import { ExpensePost } from '../../../model/ExpensePost';
import { ExpenseService } from '../../../services/ExpenseService';
import { FormsModule } from '@angular/forms';
import { GroupService } from '../../../services/GroupService';
import { ActivatedRoute } from '@angular/router';

type ExpenseRow = {
  participantId: string | null;
};
@Component({
  selector: 'app-expense-form',
  imports: [FormsModule],
  templateUrl: './expense-form.html',
  styleUrl: './expense-form.css',
})
export class ExpenseForm {
  expensePost:ExpensePost = {title:"", date:"", expenseParticipants:{}};
  expenseRows: ExpenseRow[] = [{ participantId: null }];

  constructor(public eServ:ExpenseService, public gServ:GroupService, private route:ActivatedRoute)
  {
    this.getGroupInfo();
  }

  getGroupInfo()
  {
    const id = this.route.snapshot.paramMap.get("id");
    const partId = this.route.snapshot.paramMap.get("partId");
    if(!this.gServ.openedGroup)
    {
      this.gServ.getGroupDetail(id, partId).subscribe(
          (resp) =>
          {
            this.gServ.openedGroup = resp;
            this.initializeParticipantMap();
          }
        );
    }
    else{
      this.initializeParticipantMap();
    }
  }

  initializeParticipantMap()
  {
    for(const p of this.gServ.openedGroup!.participants)
    {
      this.expensePost.expenseParticipants[p.participantId] = {paidAmount:null, share:null};
    }
  }

  addExpenseParticipant()
  {
    if(this.maxParticipantCheck())
      this.expenseRows.push({ participantId: null });
  }

  maxParticipantCheck()
  {
    return this.expenseRows.length < this.gServ.openedGroup!.participants.length;
  }

  getShare(row: any): number | null {
    if (!row.participantId) return null;
    return this.expensePost.expenseParticipants[row.participantId].share;
  }

  setShare(row: any, value: number) {
    if (!row.participantId) return;
    this.expensePost.expenseParticipants[row.participantId].share = value;
  }

  getPaid(row: any): number | null {
    if (!row.participantId) return null;
    return this.expensePost.expenseParticipants[row.participantId].paidAmount;
  }

  setPaid(row: any, value: number) {
    if (!row.participantId) return;
    this.expensePost.expenseParticipants[row.participantId].paidAmount = value;
  }
 }
