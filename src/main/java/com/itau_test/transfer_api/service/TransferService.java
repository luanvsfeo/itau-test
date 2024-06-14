package com.itau_test.transfer_api.service;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TransferService {

    RequestService requestService;


    public void generate(String transferDataJson){

        JSONObject transferDataObject =  new JSONObject(transferDataJson);

        // bate na api de cadastro
        boolean receivingClientExists = clientExists(transferDataObject.getString("receiveClientId"));

        if(receivingClientExists){
            // bate na api de contgs
            String sendingClientAccount = getAccountData(transferDataObject.getString("receiveClientAccount"));

            canDoTheTransfer(new JSONObject(sendingClientAccount), transferDataObject);
        }else{
            // nao pode realizar a transfencia, conta nao existe
        }

    }


    boolean clientExists(String clientId){
        // buscar o cliente e verifica se ele existe
        ResponseEntity<String> stringResponseEntity = requestService.makeRequestWithoutBody("localhost:8080/clientes/" + clientId, HttpMethod.GET);// todo - trocar isso aqui

        return stringResponseEntity.getStatusCode().value() == 200;
    }

    String getAccountData(String accountId){
        // buscar a conta do client que esta fazendo a transferencia
       return requestService.makeRequestWithoutBody("localhost:8080/contas/" + accountId, HttpMethod.GET).getBody();  // todo - trocar isso aqui
    }

    boolean canDoTheTransfer(JSONObject accountData, JSONObject transferData){

        //validar se a conta esta ativa

       // validar se essa conta tem saldo necessario para transferencia'

        // validar se nao excedeu o limite diario de transferencia

        if(accountData.getBoolean("ativo") && accountData.getDouble("limiteDiario") < transferData.getDouble("amount")){
            // faz a transferencia
            return true;
        }else {
            // nao faz a transferencia
            return false;
        }
    }
}


        /*
        verificar se o cliente que vai receber a transferencia existe, usando a api de cadastro (idCliente)

        buscar os dados da conta, usando a api de contas (idConta)

        validar se a conta esta ativa

        validar se essa conta tem saldo necessario para transferencia

        apos a transferencia, realizar comunicacao com a api de BACEN
         */