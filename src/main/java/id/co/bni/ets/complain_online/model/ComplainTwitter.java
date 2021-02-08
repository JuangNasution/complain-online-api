package id.co.bni.ets.complain_online.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Juang Nasution
 */
@Entity
public class ComplainTwitter implements Serializable {

    @Id
    private String noComplain;
    private String idStatus;
    private String username;
    private String subject;
    private String category;
    private String complainDetail;
    private String complainResponse;
    private Integer status;
    private Date createdDate;
    private Date responseDate;

    public String getNoComplain() {
        return noComplain;
    }

    public void setNoComplain(String noComplain) {
        this.noComplain = noComplain;
    }

    public String getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(String idStatus) {
        this.idStatus = idStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public String getComplainResponse() {
        return complainResponse;
    }

    public void setComplainResponse(String complainResponse) {
        this.complainResponse = complainResponse;
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

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
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
        if (!(object instanceof ComplainTwitter)) {
            return false;
        }
        ComplainTwitter other = (ComplainTwitter) object;
        if ((this.noComplain == null && other.noComplain != null) || (this.noComplain != null && !this.noComplain.equals(other.noComplain))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id.co.bni.ets.complain_online.model.ComplainTwitter[ id=" + noComplain + " ]";
    }

}
