import dayjs from 'dayjs/esm';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { ITurmaACC } from 'app/entities/turma-acc/turma-acc.model';
import { ITipoAtividade } from 'app/entities/tipo-atividade/tipo-atividade.model';
import { Modalidade } from 'app/entities/enumerations/modalidade.model';
import { StatusCertificado } from 'app/entities/enumerations/status-certificado.model';

export interface ICertificado {
  id?: number;
  titulo?: string | null;
  descricao?: string | null;
  dataEnvio?: dayjs.Dayjs | null;
  observacao?: string | null;
  modalidade?: Modalidade | null;
  chCuprida?: number | null;
  pontuacao?: number | null;
  status?: StatusCertificado | null;
  caminhoArquivo?: string | null;
  usuario?: IUsuario | null;
  turmaAcc?: ITurmaACC | null;
  tipoAtividade?: ITipoAtividade | null;
}

export class Certificado implements ICertificado {
  constructor(
    public id?: number,
    public titulo?: string | null,
    public descricao?: string | null,
    public dataEnvio?: dayjs.Dayjs | null,
    public observacao?: string | null,
    public modalidade?: Modalidade | null,
    public chCuprida?: number | null,
    public pontuacao?: number | null,
    public status?: StatusCertificado | null,
    public caminhoArquivo?: string | null,
    public usuario?: IUsuario | null,
    public turmaAcc?: ITurmaACC | null,
    public tipoAtividade?: ITipoAtividade | null
  ) {}
}

export function getCertificadoIdentifier(certificado: ICertificado): number | undefined {
  return certificado.id;
}
