import { ITurmaACC } from 'app/entities/turma-acc/turma-acc.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface ICurso {
  id?: number;
  nomeCurso?: string;
  sigla?: string | null;
  turmas?: ITurmaACC[] | null;
  usuarios?: IUsuario[] | null;
}

export class Curso implements ICurso {
  constructor(
    public id?: number,
    public nomeCurso?: string,
    public sigla?: string | null,
    public turmas?: ITurmaACC[] | null,
    public usuarios?: IUsuario[] | null
  ) {}
}

export function getCursoIdentifier(curso: ICurso): number | undefined {
  return curso.id;
}
