# Sistema de Monitoramento Ambiental 
Este repositório contém a evolução da atividade da semana passada (focada no padrão Observer). O objetivo desta nova iteração foi implementar novos Padrões de Projeto para resolver problemas arquiteturais, melhorar a segurança, facilitar integrações e otimizar a criação de objetos.

## Objetivo da Refatoração

O sistema original simulava uma rede de monitoramento ambiental na Amazônia, onde Universidades assinavam atualizações de sensores (Temperatura, pH e Umidade). Embora funcional, o sistema carecia de controle de acesso, gestão centralizada e integração com sistemas legados. 

Para resolver isso, decidi expandir o projeto e implementar **novos padrões de projeto** integrados ao Observer original.

## Padrões de Projeto Aplicados
- **Observer**  = desacoplamento entre a coleta de dados da Estação e o envio das informações para as Universidades interessadas. 
- **Singleton** =  criação da Central de Monitoramento, que garante que exista um único registro global em memória armazenando todas as estações de monitoramento do país, evitando instâncias duplicadas ou dados perdidos. 
- **Factory**   = criação da EstacaoFactory que remove a complexidade do Main. Ao solicitar a criação de uma estação, a fábrica instancia o objeto e já o cadastra automaticamente na Central (Singleton).
-  **Proxy**    =  criação do ProxySegurancaEstacao que atua como uma camada de proteção. Nenhuma alteração de temperatura, pH ou umidade pode ser feita nos sensores reais sem a adição de uma senha válida, prevenindo adulteração de dados. 
-  **Adapter**  = criação do AdapterEstrangeiro, permitindo que a UniversidadeOxford, que possui uma interface incompatível legada (InstituicaoEstrangeira), possa receber as atualizações do nosso sistema sem modificarmos o core do nosso Observer.
-  **Decorator**  = implementação do ZipChannel, envelopando um TCPChannel para compactar os dados trafegados internacionalmente. 
-  **Facade**     =  Oculta a complexidade de instanciar os 7 padrões anteriores, oferecendo ao cliente (Main) um acesso mais limpo. 

## Como Executar

1. Certifique-se de ter o [Java JDK](https://www.oracle.com/java/technologies/downloads/) instalado.
2. Clone o repositório e navegue até o diretório do arquivo.
3. Compile o código:
   ```bash
   javac Main.java
