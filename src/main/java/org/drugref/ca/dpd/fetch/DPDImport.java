/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * 
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.drugref.ca.dpd.fetch;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.drugref.util.JpaUtils;
import org.drugref.util.MiscUtils;

/**
 *
 * @author jaygallagher
 */
public class DPDImport {
    private static Logger logger = MiscUtils.getLogger();


    public ZipInputStream getZipStream() throws Exception {
        String sUrl = "http://www.hc-sc.gc.ca/dhp-mps/prodpharma/databasdon/txt/allfiles.zip";
        URL url = new URL(sUrl);

        //downloading some 3 MB drug data from the web, might take a while ...\n"
        ZipInputStream in = new ZipInputStream(new BufferedInputStream(url.openStream()));
        return in;
    }

    public List getDPDTablesDrop(){
        List<String> arrList = new ArrayList();
        
        arrList.add("DROP table CD_drug_product;");
        arrList.add("DROp table CD_companies;");
        arrList.add("DROp table CD_active_ingredients;");
        arrList.add("DROp table CD_drug_status;");
        arrList.add("DROp table CD_form;");
        arrList.add("DROp table CD_inactive_products;");
        arrList.add("DROp table CD_packaging;");
        arrList.add("DROp table CD_pharmaceutical_std;");
        arrList.add("DROp table CD_route;");
        arrList.add("DROp table CD_schedule;");
        arrList.add("DROp table CD_therapeutic_class;");
        arrList.add("DROp table CD_veterinary_species;");
        //arrList.add("DROP table cd2_companies;");
        return arrList;
    }



    public List getDPDTables(){
        List<String> arrList = new ArrayList();
        
        arrList.add("CREATE TABLE  cd_drug_product  (id serial  PRIMARY KEY,drug_code  int default NULL,product_categorization  varchar(80) default NULL,   class  varchar(40) default NULL,   drug_identification_number  varchar(8) default NULL,   brand_name  varchar(200) default NULL,   gp_flag  char(1) default NULL,   accession_number  varchar(5) default NULL,   number_of_ais  varchar(10) default NULL,   last_update_date  date default NULL,ai_group_no  varchar(10) default NULL,company_code int);");
        arrList.add("CREATE TABLE  CD_companies  (id serial  PRIMARY KEY,   drug_code   int default NULL,   mfr_code  varchar(5) default NULL,   company_code   int default NULL,   company_name  varchar(80) default NULL,   company_type  varchar(40) default NULL,   address_mailing_flag  char(1) default NULL,   address_billing_flag  char(1) default NULL,   address_notification_flag  char(1) default NULL,   address_other  varchar(20) default NULL,   suite_number  varchar(20) default NULL,   street_name  varchar(80) default NULL,   city_name  varchar(60) default NULL,   province  varchar(40) default NULL,   country  varchar(40) default NULL,   postal_code  varchar(20) default NULL,   post_office_box  varchar(15) default NULL);");
        arrList.add("CREATE TABLE  CD_active_ingredients  ( id serial  PRIMARY KEY,  drug_code   int default NULL,   active_ingredient_code   int default NULL,   ingredient  varchar(240) default NULL,   ingredient_supplied_ind  char(1) default NULL,   strength  varchar(20) default NULL,   strength_unit  varchar(40) default NULL,   strength_type  varchar(40) default NULL,   dosage_value  varchar(20) default NULL,   base  char(1) default NULL,   dosage_unit  varchar(40) default NULL,   notes  text);");
        arrList.add("CREATE TABLE  CD_drug_status  (id serial  PRIMARY KEY,   drug_code   int default NULL,   current_status_flag  char(1) default NULL,   status  varchar(40) default NULL,   history_date  date default NULL);");
        arrList.add("CREATE TABLE  CD_form  (id serial  PRIMARY KEY,   drug_code   int default NULL,   pharm_CD_form_code   int default NULL,   pharmaceutical_CD_form  varchar(40) default NULL);");
        arrList.add("CREATE TABLE  CD_inactive_products  (id serial  PRIMARY KEY,   drug_code   int default NULL,   drug_identification_number  varchar(8) default NULL,   brand_name  varchar(200) default NULL,   history_date  date default NULL);");
        arrList.add("CREATE TABLE  CD_packaging  (id serial  PRIMARY KEY,   drug_code   int default NULL,   upc  varchar(12) default NULL,   package_size_unit  varchar(40) default NULL,   package_type  varchar(40) default NULL,   package_size  varchar(5) default NULL,   product_inforation  varchar(80) default NULL);");
        arrList.add("CREATE TABLE  CD_pharmaceutical_std  (id serial  PRIMARY KEY,   drug_code   int default NULL,   pharmaceutical_std  varchar(40) default NULL);");
        arrList.add("CREATE TABLE  CD_route  (id serial  PRIMARY KEY,   drug_code   int default NULL,   route_of_administration_code   int default NULL,   route_of_administration  varchar(40) default NULL);");
        arrList.add("CREATE TABLE  CD_schedule  (id serial  PRIMARY KEY,   drug_code   int default NULL,   schedule  varchar(40) default NULL);");
        arrList.add("CREATE TABLE  CD_therapeutic_class  (id serial  PRIMARY KEY,   drug_code   int default NULL,   tc_atc_number  varchar(8) default NULL,   tc_atc  varchar(120) default NULL,   tc_ahfs_number  varchar(20) default NULL,   tc_ahfs  varchar(80) default NULL);");
        arrList.add("CREATE TABLE  cd_veterinary_species  (id serial  PRIMARY KEY,   drug_code   int default NULL,   vet_species  varchar(80) default NULL,   vet_sub_species  varchar(80) default NULL);");

        return arrList;
    }
    
    public List importCompanyCodetoDrugProduct(){
        List<String> arrList = new ArrayList();
        
        arrList.add("create index cd_company_drug_code_idx on cd_companies(drug_code);");
        arrList.add("create index cd_drug_code_idx on cd_drug_product(drug_code);");
        arrList.add("update cd_drug_product set company_code=(select company_code from cd_companies where cd_companies.drug_code =  cd_drug_product.drug_code);");
        return arrList;
    }



    public List dropSearchTables(){
        List<String> arrList = new ArrayList();
        arrList.add("drop table cd_drug_search;");
        arrList.add("drop table link_generic_brand;");
        return arrList;
    }
    public List getCreateSearchTables(){
        List<String> arrList = new ArrayList();

        arrList.add("CREATE TABLE  cd_drug_search  (   id serial  PRIMARY KEY,   drug_code  varchar(30),   category   int,   name  text default NULL);");
        arrList.add("create table link_generic_brand(pk_id serial  PRIMARY KEY,   id integer,    drug_code varchar(30));");
        return arrList;
    }



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        DPDImport imp = new DPDImport();

        long timeTaken = imp.doItDifferent();  // executeOn(entities);
        System.out.println("GOING OUT after "+timeTaken);
    }



    private void insertLines(EntityManager entityManager,List<String> sqlLines){

             for (String sql : sqlLines) {
                logger.debug(sql);
                Query query = entityManager.createNativeQuery(sql);
                try{
                query.executeUpdate();
                }catch(Exception e){
                    String getMsg = e.getMessage();
                    System.out.println("ERROR :"+getMsg);
                }
             }
    }


    public long doItDifferent() {
        long startTime = System.currentTimeMillis();
        EntityManager entityManager = JpaUtils.createEntityManager();
        try {
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();

            insertLines(entityManager,getDPDTablesDrop());
//             List<String> sqlLines = getDPDTablesDrop();
//
//             for (String sql : sqlLines) {
//                logger.debug(sql);
//                Query query = entityManager.createNativeQuery(sql);
//                try{
//                query.executeUpdate();
//                }catch(Exception e){
//                    String getMsg = e.getMessage();
//                    System.out.println("ERROR :"+getMsg);
//                }
//             }

             insertLines(entityManager, getDPDTables());
//             sqlLines = getDPDTables();
//
//             for (String sql : sqlLines) {
//                logger.debug(sql);
//                Query query = entityManager.createNativeQuery(sql);
//                query.executeUpdate();
//             }

             tx.commit();
             RecordParser recordParse = new RecordParser();
             try{
                 ZipInputStream zipStream = getZipStream();
                 ZipEntry ze = null;
                 while ((ze = zipStream.getNextEntry()) != null) {
                        System.out.println(ze.getName());
                        recordParse.getDPDObject(ze.getName(),zipStream,entityManager);
                       
                        //entityManager.flush();
                 }

             }catch(Exception e){
                 e.printStackTrace();
             }

             tx.begin();
             insertLines(entityManager,dropSearchTables());
//             sqlLines = dropSearchTables();
//
//             for (String sql : sqlLines) {
//                logger.debug(sql);
//                Query query = entityManager.createNativeQuery(sql);
//                query.executeUpdate();
//             }
             insertLines(entityManager,getCreateSearchTables());
//             sqlLines = getCreateSearchTables();
//
//             for (String sql : sqlLines) {
//                logger.debug(sql);
//                Query query = entityManager.createNativeQuery(sql);
//                query.executeUpdate();
//             }
             tx.commit();
    
             ConfigureSearchData searchData = new ConfigureSearchData();
             searchData.importSearchData(entityManager);


            System.out.println("committing");
            //tx.commit();
        } finally {

            System.out.println("closing entityManager");
            JpaUtils.close(entityManager);
            System.out.println("entityManagerClosed");
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;

    }
}
//    private static final Localizer _loc = Localizer.forPackage(DPDImport.class);
//    //(MappingToolTask.class);
//    protected MappingTool.Flags flags = new MappingTool.Flags();

//    public void executeOn(String[] files)
//            throws Exception {
//        //if (MappingTool.ACTION_IMPORT.equals(flags.action))
//        //    assertFiles(files);
//        StringWriter sWriter = new StringWriter();
//        ClassLoader toolLoader = AccessController.doPrivileged(J2DoPrivHelper.getClassLoaderAction(MappingTool.class));
//        ClassLoader loader = toolLoader;
//        MultiLoaderClassResolver resolver = new MultiLoaderClassResolver();
//
//        String file = new String("/Users/jaygallagher/dd.dd");
//
//        String schemaFile = new String("/Users/jaygallagher/schemaFiledd.dd");
//        String sqlFile = new String("/Users/jaygallagher/sqlFiledd.dd");
//
//
//        resolver.addClassLoader(this.getClass().getClassLoader());
//        //if (tmpClassLoader) {
////            loader = AccessController.doPrivileged(J2DoPrivHelper
////                    .newTemporaryClassLoaderAction(getClassLoader()));
////            resolver.addClassLoader(loader);
//        //}
//        resolver.addClassLoader(toolLoader);
//
//        if (flags.meta && MappingTool.ACTION_ADD.equals(flags.action)) {
//            flags.metaDataFile = Files.getFile(file, loader);
//        } else {
//            flags.mappingWriter = Files.getWriter(file, loader);
//        }
//
//        flags.schemaWriter = Files.getWriter(schemaFile, loader);
//        flags.sqlWriter = Files.getWriter(sqlFile, loader);
//
//        //JDBCConfiguration conf = (JDBCConfiguration) newConfiguration();
//        //conf.setClassResolver(resolver);
//
//        //System.out.println(SpringUtils.getBean("JDBCConfiguration").getClass().getName());
//
//        JDBCConfiguration conf = new JDBCConfigurationImpl();//;(JDBCConfigurationImpl)super.newConfiguration();
//
//        //conf.setConnectionDriverName(ConfigUtils.getProperty("db_driver"));
//        //conf.setConnectionURL(ConfigUtils.getProperty("db_url"));
//        //conf.setConnectionUserName(ConfigUtils.getProperty("db_user"));
//        //conf.setConnectionPassword(ConfigUtils.getProperty("db_password"));
//
//
//        //MetaDataRepository repo = conf.getMetaDataRepositoryInstance(); //.getMetaDataFactory().;
//        //repo.endConfiguration();
//        conf.setClassResolver(resolver);
//
//        MetaDataFactory mdf2 = conf.newMetaDataFactoryInstance();
//        System.out.println("mdf2 " + mdf2);
//
//
//        Collection classes = null;
//
//        ObjectValue metaRepositoryPlugin = new MetaDataRepositoryValue();
//
//        ObjectValue ObValue = new ObjectValue("MetaDataRepository");
//
//
//        Configurations.newInstance("org.apache.openjpa.meta.MetaDataRepository", conf, "", loader);
//
//
//        Object ob = ObValue.newInstance("org.apache.openjpa.meta.MetaDataRepository", MetaDataRepository.class, conf, true);
//
//        metaRepositoryPlugin.instantiate(MetaDataRepository.class, conf);
//
//
//        //classes
//        MetaDataFactory mdf = conf.newMetaDataFactoryInstance();
//        System.out.println("MDF " + mdf + "  " + conf.getMetaDataRepository());
//        MappingRepository mappingRepo = conf.getMappingRepositoryInstance();
//        mappingRepo.loadPersistentTypes(true, loader);
//        System.out.println(classes);
//
//
//
//
//        System.out.println("db url " + conf.getConnectionURL());
//        System.out.println("PASSWPRD " + conf.getConnectionPassword());
//
//        if (!MappingTool.run(conf, files, flags, loader)) {
//            System.out.println("Didn't load!");
//        }
//        //throw new BuildException(_loc.get("bad-conf", "MappingToolTask").getMessage());
//    }

//    public ConfigurationImpl newConfiguration() {
//        //System.setProperty("catalina.base", System.getProperty("java.io.tmpdir"));
//
//        JDBCConfigurationImpl conf = new JDBCConfigurationImpl();//;(JDBCConfigurationImpl)super.newConfiguration();
//        //conf;
//                /*
//        conf.setConnectionDriverName("com.mysql.jdbc.Driver");//org.gjt.mm.mysql.Driver");//ConfigUtils.getProperty("db_driver"));
//        conf.setConnectionURL("jdbc:mysql://127.0.0.1:3306/drugref");//ConfigUtils.getProperty("db_url"));
//        conf.setConnectionUserName("root");//ConfigUtils.getProperty("db_user"));
//        conf.setConnectionPassword("liyi");//ConfigUtils.getProperty("db_password"));
//         */
//
//        conf.setConnectionDriverName(ConfigUtils.getProperty("db_driver"));
//        conf.setConnectionURL(ConfigUtils.getProperty("db_url"));
//        conf.setConnectionUserName(ConfigUtils.getProperty("db_user"));
//        conf.setConnectionPassword(ConfigUtils.getProperty("db_password"));
//
//
//        return (conf);
//
//    }
//}
/*
 echo "downloading some 3 MB drug data from the web, might take a while ...\n"
#wget -c http://www.hc-sc.gc.ca/hpfb-dgpsa/tpd-dpt/txt/allfiles.zip
mkdir drugrefImport
cd drugrefImport
wget -c http://www.hc-sc.gc.ca/dhp-mps/prodpharma/databasdon/txt/allfiles.zip
# unzip the data into the current directory
unzip allfiles.zip
cd ..
# create the temporary tables for import purposes
echo "\n --> creating temporary tables for import purposes ...\n"
$PATH_TO_PSQL/psql drugref2 -f create_dpd_tables.psql
# convert the comma separated text files into proper importable tab spearated unquoted data
echo "\n --> converting the text files into a proper importable format ...\n"
#cp ../strip_quotation.py .
python import_dpd.py
# import the converted tab separated unquoted data into the temporary tables
echo "\n --> now importig the data into the temporary tables ...\n"
$PATH_TO_PSQL/psql drugref2 -f import_dpd_tables.psql
# do some house keeping with the imported tables
echo "\n --> cleaning up the imported table, preparing the import into drugref ...\n"
$PATH_TO_PSQL/psql drugref2 -f dpd_nodupes.psql

# create extra tables to make searching easier
echo "\n --> creating extra tables  ...\n"
$PATH_TO_PSQL/psql drugref2 -f create_extra_tables.psql

#import the data into extra tables for easier searching
echo "\n --> importing data into searching  tables ...\n"
python import_search_data.py


#create tables for interaction data
echo "\n --> creating interaction tables ... \n"
$PATH_TO_PSQL/psql drugref2 -f holbrooktables.psql

#import interaction data
echo "\n --> importing interaction data ... \n"
$PATH_TO_PSQL/psql drugref2 -f interactions-holbrook.dump


#clean up the temp tables
oscar@oscar01:/usr/local/drugref/DPD$

 */