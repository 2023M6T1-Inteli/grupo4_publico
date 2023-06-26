<p align="center">
<a href= "https://www.inteli.edu.br/"><img src="img/inteli-logo.png" alt="Inteli - Instituto de Tecnologia e Liderança" border="0" width="30%"></a>
</p>
<p align="center">
<img src="img/cutsolvers-logo.svg" alt="Grupo CutSolvers" border="0" width="30%">
</p>

# Introdução

Este é um dos repositórios do projeto de alunos do Inteli referente ao à resolução de um problema de _cutting stock problem_ do 1º semestre de 2023. Este projeto foi desenvolvido por alunos do Módulo 6 do curso de Ciência da Computação.

# Projeto: _Solução de otimização de corte de bobinas de papel_

# Grupo: _CutSolvers_

# Alunos

- [Arthur Alberto Cardoso Reis](https://www.linkedin.com/in/arthureis03/)
- [Cristiane Andrade Coutinho](https://www.linkedin.com/in/cristianeacoutinho/)
- [Elias Biondo](https://linkedin.com/in/eliasbiondo)
- [Gabriel Caetano Nhoncanse](https://www.linkedin.com/in/gabrielcaetanonhoncanse/)
- [Giovana Lisbôa Thomé](https://www.linkedin.com/in/giovana-lisboa-thome/)
- [Renato Silva Machado](https://www.linkedin.com/in/renatosilvamachado)
- [Yasmin Vitória Rocha de Jesus](https://www.linkedin.com/in/yasminvit%C3%B3riarocha/)

# Descrição

Este projeto consiste na elaboração de um algoritmo de otimização para a minimização de perdas e desperdícios no processo de corte de rolos de papel, aumentando a eficiência e a produtividade do processo de corte. Utilizamos, para isso, conceitos relacionados à pesquisa operacional. Por meio disso, o sistema desenvolvido realiza a combinação das larguras de bobinas, buscando alcançar uma solução ótima aproximada. Com o auxílio dos algoritmos Simplex e Branch and Price, conseguimos otimizar o processo de corte garantindo um rápido processamento.


# Guia de instalação e preparação do ambiente de desenvolvimento

Este guia irá te ajudar a instalar e rodar a aplicação em seu ambiente de desenvolvimento local.

## Pré-requisitos

Para rodar este projeto, você precisa ter instalado na sua máquina:

1. Node.js versão 18.16.0
2. Docker
3. Open-JDK-19

## Instalação

### Preparação do ambiente

Não se faz necessária nenhuma preparação do ambiente de desenvolvimento em termos de variáveis ambientes e correlatos.

### Frontend

1. Navegue até a pasta do frontend: `cd /codigo/frontend`.
2. Instale as dependências do projeto: `npm install`.
3. Inicie o servidor de desenvolvimento: `npm start`.

Depois desses passos, o frontend deve estar rodando em http://localhost:3000.

### Banco de dados

1. Garanta que o Docker esteja instalado em seu computador.
2. Navegue até a pasta do Docker: `cd /docker`.
3. Rode o comando para subir o banco de dados: `docker compose up -d`.

### Backend (Planejador)

1. Garanta que o Open-JDK-19 esteja instalado na sua máquina.
2. Abra o projeto no seu ambiente de desenvolvimento Java.
3. Faça o build e rode o projeto.

## Teste da aplicação
Para teste de usabilidade, um arquivo exemplo de input modificável está localizado em `./uploads/exemplo_input.xlsx`. O padrão do arquivo deve ser seguido para possiblitar a leitura e processamento dos dados fornecidos ao sistema.

# Licença

Este projeto é licenciado sob a licença Apache 2.0 - veja o arquivo LICENSE para detalhes.
