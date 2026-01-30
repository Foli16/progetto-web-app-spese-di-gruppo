import { Component } from '@angular/core';
import { ExpensePost } from '../../../model/ExpensePost';
import { ExpenseService } from '../../../services/ExpenseService';
import { FormsModule } from '@angular/forms';
import { GroupService } from '../../../services/GroupService';
import { ActivatedRoute } from '@angular/router';

type ExpenseRow = {
  participantId: string | null;
  tempShare: number | null;
  tempPaid: number | null;
};
@Component({
  selector: 'app-expense-form',
  imports: [FormsModule],
  templateUrl: './expense-form.html',
  styleUrl: './expense-form.css',
})
export class ExpenseForm {
  expensePost:ExpensePost = {title:"", date:"", expenseParticipants:{}};
  expenseRows: ExpenseRow[] = [{ participantId: null, tempShare:null, tempPaid:null }];

  constructor(public eServ:ExpenseService, public gServ:GroupService, private route:ActivatedRoute)
  {
    this.getGroupInfo();
  }

  getGroupInfo()
  {
    if(!this.gServ.openedGroup)
    {
      const id = this.route.snapshot.paramMap.get("id");
      const partId = this.route.snapshot.paramMap.get("partId");
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
      this.expenseRows.push({ participantId: null, tempShare:null, tempPaid:null  });
  }

  maxParticipantCheck()
  {
    return this.expenseRows.length < this.gServ.openedGroup!.participants.length;
  }

  onParticipantChange(row:ExpenseRow)
  {
    if(!row.participantId)
      return;
    const participant = this.expensePost.expenseParticipants[row.participantId];
    if (row.tempShare !== null) {
      participant.share = row.tempShare;
    }

    if (row.tempPaid !== null) {
      participant.paidAmount = row.tempPaid;
    }
  }

  getShare(row: any): number | null {
    // if (!row.participantId) return row.tempShare;
    // return this.expensePost.expenseParticipants[row.participantId].share;
    return row.tempShare;
  }

  setShare(row: any, value: number) {
    if (!row.participantId)
    {
      row.tempShare = value;
      return;
    }
    row.tempShare = value;
    this.expensePost.expenseParticipants[row.participantId].share = value;
  }

  getPaid(row: any): number | null {
    // if (!row.participantId) return row.tempPaid;
    // return this.expensePost.expenseParticipants[row.participantId].paidAmount;
    return row.tempPaid;
  }

  setPaid(row: any, value: number) {
    if (!row.participantId)
    {
      row.tempPaid = value;
      return;
    }
    row.tempPaid = value;
    this.expensePost.expenseParticipants[row.participantId].paidAmount = value;
  }
 }
