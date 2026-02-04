import { GroupPreviewGet } from "./GroupPreviewGet"

export type GroupDetailGet = GroupPreviewGet &
{
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
        expenseParticipants:
        {
            expenseParticipantName:string,
            paidAmount:number,
            share:number
        }[]
    }[]
}