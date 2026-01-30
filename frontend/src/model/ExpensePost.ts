export interface ExpensePost
{
    title:string,
    date:string,
    expenseParticipants:
    {
        [participantId:string]:
        {
            paidAmount:number | null,
            share:number | null
        }
    }
}