/*
 * Copyright (C) 2014 Sunny Patel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package frontoffice;

import frontoffice.base.DBConnection;
import java.awt.print.PrinterJob;
import java.util.HashMap;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Sunny Patel
 */
class Report extends DBConnection {
    JasperReport jRpt;
    JasperPrint jPrint;
    
    /*
        to call this through
        Report r = new Report();
        r.reportdata.put("saleid", new Integer(1001));
    */
    public HashMap reportdata = new HashMap();
    
    private String ReportLocation = "";
    private String RptEndName = "";
    
    JFrame report = new JFrame("Report Viewer");
    
    public Report(String rlArg) {
        ReportLocation = rlArg;
        RptEndName = rlArg;
    }
    
    public void compileReport() {
        try {
            conn = getConnection();
            jRpt = JasperCompileManager.compileReport(ReportLocation);
            jPrint = JasperFillManager.fillReport(jRpt, reportdata, conn);
        }  catch( Exception a ){
            a.printStackTrace();
        }
    }
    
    public void viewReport() {
        try {
            JRViewer viewer = new JRViewer(jPrint);
            viewer.setOpaque(true);
            report.add(viewer);
            report.setSize(701, 1001);
            report.setVisible(true);
        } catch (Exception a) {
            a.printStackTrace();
        }
    }
    
    public void printReport(String printPrinter) {
        int selectedService = 0;
        try {
            JasperPrintManager.printReport(jPrint, false);
            PrinterJob pj = PrinterJob.getPrinterJob();
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
            for( int i = 0; i < services.length; i ++) {
                if ( services[i].getName().toUpperCase().contains("printprinter") ) {
                    selectedService = i;
                    break;
                }
            }
            pj.setPrintService(services[selectedService]);
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            JRPrintServiceExporter exporter = new JRPrintServiceExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jPrint);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, services[selectedService]);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, services[selectedService].getAttributes());
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
            exporter.exportReport();
        } catch(Exception a) {
            a.printStackTrace();
        }
    }
}