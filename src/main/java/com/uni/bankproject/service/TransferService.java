package com.uni.bankproject.service;
import com.uni.bankproject.entity.Transfer;
import com.uni.bankproject.repository.TransferImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferService{

    @Autowired
    TransferImplementation tr;

    public String transferMoney (Transfer transfer) {
        if (tr.verifyDestinationAccount(transfer.getDestinationAccount())) {
            if (transfer.getOriginAccount().equals(transfer.getDestinationAccount())) {
                return "No puedes transferir dinero a la misma cuenta";
            }
            if (transfer.getAmount() <= 0) {
                return "La cantidad a transferir debe ser mayor a 0";
            }
            if (tr.VerifyTypeAccount(transfer.getOriginAccount(), "Credit")) {
                if (tr.VerifyBalance(transfer.getOriginAccount(), transfer.getAmount())) {
                    tr.transferMoney(transfer.getOriginAccount(), transfer.getDestinationAccount(), transfer.getAmount());
                    tr.save(transfer);
                    return "Transferencia realizada con éxito";
                } else {
                    return "No tienes cupo disponible en tu cuenta de crédito";
                }
            } else {
                if (tr.verifyMoney(transfer.getOriginAccount(), transfer.getAmount())) {
                    tr.transferMoney(transfer.getOriginAccount(), transfer.getDestinationAccount(), transfer.getAmount());
                    tr.save(transfer);
                    return "Transferencia realizada con éxito";
                } else {
                    return "No tienes suficiente dinero en tu cuenta";
                }
            }
        } else {
            return "La cuenta de destino no existe";
        }
    }

    public List<Transfer> getTransfersByAccount(String id){
        List<Transfer> transfer = tr.getTransfersByAccountNumber(id);

        if (transfer.isEmpty()){
            return null;
        } else {
            return transfer;
        }
    }


}
