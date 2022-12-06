export class Valutazione {
    private note:string;
    private valutazione:string;

    constructor(valutazione:any){
        this.note=valutazione.note;
        this.valutazione=valutazione.valutazione;
    }

    public getNote():string{
        return this.note;
    }
    public setNote(note:string):void{
        this.note=note;
    }

    public getValutazione():string{
        return this.valutazione;
    }
    public setValutazione(valutazione:string):void{
        this.valutazione=valutazione;
    }


}
