# FASE 36 — Google Play Console

## Estado

**Preparação concluída; ativação externa pendente.** Criar a conta exige decisão do proprietário, aceite contratual, pagamento e verificação de identidade. Nenhuma conta foi criada pelo código.

## Escolha obrigatória

| Conta | Escolher quando | Preparar |
| --- | --- | --- |
| Pessoal | o publicador é uma pessoa física e assumirá direitos, pagamentos e exposição dos dados exigidos | documento oficial, endereço, telefone, e-mail e forma de pagamento próprios |
| Organização | existe pessoa jurídica que será titular do app | dados legais, D-U-N-S quando solicitado, site/domínio, contato autorizado e documentos da organização |

Não escolha “organização” apenas por aparência profissional e não escolha “pessoal” se o ativo pertence juridicamente a uma empresa.

## Checklist de ativação

- [ ] Definir titular pessoal ou organização com responsável contábil/jurídico.
- [ ] Criar uma Conta Google institucional com MFA e recuperação controlada.
- [ ] Abrir a conta no Play Console e aceitar os contratos vigentes.
- [ ] Pagar a taxa apresentada pelo Google usando meio autorizado.
- [ ] Concluir verificação de identidade, telefone, e-mail e endereço.
- [ ] Registrar nome público do desenvolvedor e contatos de suporte.
- [ ] Conceder acesso nominal com menor privilégio; nunca compartilhar senha.
- [ ] Cadastrar URL HTTPS da política de privacidade quando o domínio estiver ativo.
- [ ] Configurar notificações de segurança e guardar recibos/evidências.

## Teste para contas pessoais

Contas pessoais novas podem precisar concluir teste fechado antes do acesso à produção. O planejamento reserva pelo menos 14 dias contínuos e 12 testadores inscritos, mas o requisito exibido no Play Console no momento da publicação prevalece, pois as regras podem mudar. Consulte a [orientação oficial sobre requisitos de teste](https://support.google.com/googleplay/android-developer/answer/14151465?hl=pt-BR).

## Próxima ação do proprietário

Informar ao projeto: tipo da conta escolhida, nome legal/público do desenvolvedor, país, e-mail de suporte e confirmação de que a verificação terminou. Não enviar documentos, senha, códigos MFA ou dados de pagamento ao repositório.

Depois da ativação, avançar para a FASE 37 e criar o aplicativo no Console com o `applicationId` definitivo.
