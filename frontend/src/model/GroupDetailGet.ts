import { GroupPreviewGet } from "./GroupPreviewGet"

export interface GroupDetailGet
{
    basicInfo:GroupPreviewGet,
    participants:
    {
        participantId:string,
        participantName:string,
        participantTotalExpense:number,
        balance:number
    }[],
    expenses:
    {
        expenseTitle:string,
        amount:number,
        date:string,
        creationTime:Date,
        expenseParticipants:
        {
            expenseParticipantName:string,
            paidAmount:number,
            share:number
        }[]
    }[]
}