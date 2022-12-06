export class Valutazione {
    private idValutazione:any;
    private note:string;
    private valutazione:string;
    private idTx:string;

    constructor(valutazione:any){
        this.idValutazione=valutazione.idValutazione;
        this.note=valutazione.note;
        this.valutazione=valutazione.valutazione;
        this.idTx=valutazione.idTX;
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
    
    public getIdValutazione():string{
        return this.idValutazione;
    }

    public getIdTX():string{
        return this.idTx;
    }


}
