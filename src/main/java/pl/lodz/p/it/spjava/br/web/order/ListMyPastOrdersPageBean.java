/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.br.dto.OrderDTO;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "listMyPastOrdersPageBean")
@ViewScoped
public class ListMyPastOrdersPageBean implements Serializable {

    @Inject
    private OrderControllerBean orderControllerBean;

    private List<OrderDTO> listMyPastOrders;

    private DataModel<OrderDTO> dataModelOrderes;

    public ListMyPastOrdersPageBean() {
    }

    public DataModel<OrderDTO> getDataModelOrderes() {
        return dataModelOrderes;
    }

    @PostConstruct
    public void initListMyPastOrderes() {
        Date currentDate = new Date(System.currentTimeMillis());
        try {
            listMyPastOrders = orderControllerBean.listMyPastOrders(currentDate);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListMyPastOrdersPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelOrderes = new ListDataModel<>(listMyPastOrders);
    }
}
