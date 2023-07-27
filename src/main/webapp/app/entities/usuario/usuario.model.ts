import dayjs from 'dayjs/esm';
import { ITurmaACC } from 'app/entities/turma-acc/turma-acc.model';
import { ICurso } from 'app/entities/curso/curso.model';
import { Perfil } from 'app/entities/enumerations/perfil.model';

export interface IUsuario {
  id?: number;
  nome?: string;
  login?: string;
  senha?: string;
  dataCadastro?: dayjs.Dayjs | null;
  ultimoAcesso?: dayjs.Dayjs | null;
  perfil?: Perfil;
  turmas?: ITurmaACC[] | null;
  cursos?: ICurso[] | null;
}

export class Usuario implements IUsuario {
  constructor(
    public id?: number,
    public nome?: string,
    public login?: string,
    public senha?: string,
    public dataCadastro?: dayjs.Dayjs | null,
    public ultimoAcesso?: dayjs.Dayjs | null,
    public perfil?: Perfil,
    public turmas?: ITurmaACC[] | null,
    public cursos?: ICurso[] | null
  ) {}
}

export function getUsuarioIdentifier(usuario: IUsuario): number | undefined {
  return usuario.id;
}
