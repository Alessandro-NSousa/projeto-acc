import dayjs from 'dayjs/esm';

export interface ITipoAtividade {
  id?: number;
  nome?: string;
  descricao?: string | null;
  numeroPontos?: number | null;
  dataCriacao?: dayjs.Dayjs | null;
}

export class TipoAtividade implements ITipoAtividade {
  constructor(
    public id?: number,
    public nome?: string,
    public descricao?: string | null,
    public numeroPontos?: number | null,
    public dataCriacao?: dayjs.Dayjs | null
  ) {}
}

export function getTipoAtividadeIdentifier(tipoAtividade: ITipoAtividade): number | undefined {
  return tipoAtividade.id;
}
