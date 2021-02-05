package id.co.bni.ets.complain_online.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Juang Nasution
 */
@Entity
@Table(schema = "dbo", name = "complain_list")
public class Complain implements Serializable {

    @Id
    private String noComplain;
    private String subject;
    private String category;
    private String complainDetail;
    private String complainResponse;
    private Integer status;
    private Date createdDate;
    private Date doneDate;
    private Integer customerId;
    private Integer cardId;
    @Transient
    private String cardNumber;

    public String getNoComplain() {
        return noComplain;
    }

    public void setNoComplain(String noComplain) {
        this.noComplain = noComplain;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComplainDetail() {
        return complainDetail;
    }

    public void setComplainDetail(String complainDetail) {
        this.complainDetail = complainDetail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(Date doneDate) {
        this.doneDate = doneDate;
    }

    public String getComplainResponse() {
        return complainResponse;
    }

    public void setComplainResponse(String complainResponse) {
        this.complainResponse = complainResponse;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (noComplain != null ? noComplain.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the noComplain fields are not set
        if (!(object instanceof Complain)) {
            return false;
        }
        Complain other = (Complain) object;
        if ((this.noComplain == null && other.noComplain != null) || (this.noComplain != null && !this.noComplain.equals(other.noComplain))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id.co.bni.ets.complain_online.model.ComplainList[ id=" + noComplain + " ]";
    }

}
