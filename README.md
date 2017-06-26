Desafio de Rest

Esse desafio tem como objetivo CADASTRO/LOGIN/LISTAGEM de USUARIOS

https://desafiomichel.herokuapp.com/


====================================================================================================
Enviando via GET

Listar todos os usuarios

http://{server}/usuarios/
Exemplo: http://localhost:8080/usuarios/


=====================================================================================================

Criando um usuario

http://{server}/usuario/
Exemplo: http://localhost:8080/usuario/

Enviando os dados via POST

{ 
"name": "michel", 
 "email": "michel@michel.com", 
 "password": "abc123", 
 "phones": [ { "number": "123456789", "ddd": "11" } ] 
}

=======================================================
Login

Através do metodo POST
http://{server}/login
Exemplo: http://localhost:8080/login/


Enviando os dados via POST

{
        "email": "michel@michel.com",
        "password": "abc123"
        
}

=======================================================
Listar dados dos usuários

http://{server}/usuario/{id}


Enviando os dados via GET

ID - Identificador de usuario recebido no momento de cadastro do usuario
Para Acesso é necessario Token

basta fazer a requisição enviando os parametros pelo Header

x-auth-token: {Token de acesso recebido no momento do cadastro de usuario}

Exemplo de Envio no Header
x-auth-token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0Nzc4NjU1MDYsImlkIjoiYWRtaW4iLCJvcmlnX2lhdCI6MTQ3Nzg2MTkwNn0.Pt4O5ZqDtVGEomQlnOSlxSvCGGvs-tFXmoJQ0OoXbRU

http://{HOST}/usuario/f7efbd8a9f454db6a00ab9421382a479 - Lista dados do usuario do id fornecido

====================================================
