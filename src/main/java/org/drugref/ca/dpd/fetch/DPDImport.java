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
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.drugref.util.ConfigUtils;
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
        return getZipStream(sUrl);
    }

    public ZipInputStream getInactiveZipStream() throws Exception {
            String sUrl = "http://www.hc-sc.gc.ca/dhp-mps/prodpharma/databasdon/txt/allfiles_ia.zip";
                    return getZipStream(sUrl);
    }

    public ZipInputStream getInactiveTableZipStream() throws Exception {
        String sUrl = "http://www.hc-sc.gc.ca/dhp-mps/prodpharma/databasdon/txt/inactive.zip";
        return getZipStream(sUrl);
    }

    private  ZipInputStream getZipStream(String sUrl) throws Exception {
        URL url = new URL(sUrl);
        ZipInputStream in = new ZipInputStream(new BufferedInputStream(url.openStream()));
        return in;
    }

    public List getDPDTablesDrop() {
        List<String> arrList = new ArrayList();
        //table names are case sensitive.
        String[] tableNames = {"cd_drug_product", "cd_companies", "cd_active_ingredients", "cd_drug_status", "cd_form", "cd_inactive_products",
            "cd_packaging", "cd_pharmaceutical_std", "cd_route", "cd_schedule", "cd_therapeutic_class", "cd_veterinary_species", "interactions"};
        for (String tableName : tableNames) {
            if (isTablePresent(tableName)) {
                String statement = "DROP TABLE " + tableName;
                arrList.add(statement);
            } else {
            }
        }

        p("arrList", arrList.toString());
        return arrList;
    }

    private boolean isTablePresent(String tableName) {//check if a table exists in the database
        boolean bool = false;
        Connection con = null;
        String dbURL = ConfigUtils.getProperty("db_url");
        String dbUser = ConfigUtils.getProperty("db_user");
        String dbPassword = ConfigUtils.getProperty("db_password");

        try {
            con = DriverManager.getConnection(dbURL, dbUser, dbPassword);

        } catch (SQLException e) {
            System.out.println("Connection Failed.");
            e.printStackTrace();
            bool = false;
        }
        try {
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet rs = dbm.getTables(null, null, tableName, null);
            if (rs.next()) {
                bool = true;
            } else {
                bool = false;
            }
            con.close();//release resources
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    public List getDPDTables() {
        List<String> arrList = new ArrayList();
        
        arrList.add("CREATE TABLE  cd_drug_product  (id serial  PRIMARY KEY,drug_code  int default NULL,product_categorization  varchar(80) default NULL,   class  varchar(40) default NULL,   drug_identification_number  varchar(8) default NULL,   brand_name  varchar(200) default NULL,   gp_flag  char(1) default NULL,   accession_number  varchar(5) default NULL,   number_of_ais  varchar(10) default NULL,   last_update_date  date default NULL,ai_group_no  varchar(10) default NULL,company_code int);");
        arrList.add("CREATE TABLE  cd_companies  (id serial  PRIMARY KEY,   drug_code   int default NULL,   mfr_code  varchar(5) default NULL,   company_code   int default NULL,   company_name  varchar(80) default NULL,   company_type  varchar(40) default NULL,   address_mailing_flag  char(1) default NULL,   address_billing_flag  char(1) default NULL,   address_notification_flag  char(1) default NULL,   address_other  varchar(20) default NULL,   suite_number  varchar(20) default NULL,   street_name  varchar(80) default NULL,   city_name  varchar(60) default NULL,   province  varchar(40) default NULL,   country  varchar(40) default NULL,   postal_code  varchar(20) default NULL,   post_office_box  varchar(15) default NULL);");
        arrList.add("CREATE TABLE  cd_active_ingredients  ( id serial  PRIMARY KEY,  drug_code   int default NULL,   active_ingredient_code   int default NULL,   ingredient  varchar(240) default NULL,   ingredient_supplied_ind  char(1) default NULL,   strength  varchar(20) default NULL,   strength_unit  varchar(40) default NULL,   strength_type  varchar(40) default NULL,   dosage_value  varchar(20) default NULL,   base  char(1) default NULL,   dosage_unit  varchar(40) default NULL,   notes  text);");
        arrList.add("CREATE TABLE  cd_drug_status  (id serial  PRIMARY KEY,   drug_code   int default NULL,   current_status_flag  char(1) default NULL,   status  varchar(40) default NULL,   history_date  date default NULL);");
        arrList.add("CREATE TABLE  cd_form  (id serial  PRIMARY KEY,   drug_code   int default NULL,   pharm_cd_form_code   int default NULL,   pharmaceutical_cd_form  varchar(40) default NULL);");
        arrList.add("CREATE TABLE  cd_inactive_products  (id serial  PRIMARY KEY,   drug_code   int default NULL,   drug_identification_number  varchar(8) default NULL,   brand_name  varchar(200) default NULL,   history_date  date default NULL);");
        arrList.add("CREATE TABLE  cd_packaging  (id serial  PRIMARY KEY,   drug_code   int default NULL,   upc  varchar(12) default NULL,   package_size_unit  varchar(40) default NULL,   package_type  varchar(40) default NULL,   package_size  varchar(5) default NULL,   product_inforation  varchar(80) default NULL);");
        arrList.add("CREATE TABLE  cd_pharmaceutical_std  (id serial  PRIMARY KEY,   drug_code   int default NULL,   pharmaceutical_std  varchar(40) default NULL);");
        arrList.add("CREATE TABLE  cd_route  (id serial  PRIMARY KEY,   drug_code   int default NULL,   route_of_administration_code   int default NULL,   route_of_administration  varchar(40) default NULL);");
        arrList.add("CREATE TABLE  cd_schedule  (id serial  PRIMARY KEY,   drug_code   int default NULL,   schedule  varchar(40) default NULL);");
        arrList.add("CREATE TABLE  cd_therapeutic_class  (id serial  PRIMARY KEY,   drug_code   int default NULL,   tc_atc_number  varchar(8) default NULL,   tc_atc  varchar(120) default NULL,   tc_ahfs_number  varchar(20) default NULL,   tc_ahfs  varchar(80) default NULL);");
        arrList.add("CREATE TABLE  cd_veterinary_species  (id serial  PRIMARY KEY,   drug_code   int default NULL,   vet_species  varchar(80) default NULL,   vet_sub_species  varchar(80) default NULL);");
         
        arrList.add("CREATE TABLE  interactions  (id serial PRIMARY KEY, affectingatc varchar(7), affectedatc varchar(7) default NULL, effect char(1) default NULL, significance char(1) default NULL, evidence char(1) default NULL, comment text default NULL, affectingdrug text default NULL, affecteddrug text default NULL, CONSTRAINT UNQ_ATC_EFFECT UNIQUE (affectingatc, affectedatc, effect));");

        return arrList;
    }

    public List importCompanyCodetoDrugProduct() {
        List<String> arrList = new ArrayList();

        arrList.add("create index cd_company_drug_code_idx on cd_companies(drug_code);");
        arrList.add("create index cd_drug_code_idx on cd_drug_product(drug_code);");
        arrList.add("update cd_drug_product set company_code=(select company_code from cd_companies where cd_companies.drug_code =  cd_drug_product.drug_code);");
        return arrList;
    }

    public List dropSearchTables() {
        List<String> arrList = new ArrayList();
        String[] tableNames = {"cd_drug_search", "link_generic_brand"};
        for (String tableName : tableNames) {
            if (isTablePresent(tableName)) {
                String statement = "DROP TABLE " + tableName;
                arrList.add(statement);
            } else {
            }
        }
        return arrList;
    }

    public List getCreateSearchTables() {
        List<String> arrList = new ArrayList();

        arrList.add("CREATE TABLE  cd_drug_search  (id serial  PRIMARY KEY,   drug_code  varchar(30),   category   int,   name  text default NULL);");
        arrList.add("create table link_generic_brand(pk_id serial  PRIMARY KEY,   id integer,    drug_code varchar(30));");
        return arrList;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        
        DPDImport imp = new DPDImport();
        long timeTaken = imp.doItDifferent();  // executeOn(entities);
        System.out.println("GOING OUT after " + timeTaken);
    }

    private void insertLines(EntityManager entityManager, List<String> sqlLines) {

        for (String sql : sqlLines) {
            p("sql", sql);
            logger.debug(sql);
            Query query = entityManager.createNativeQuery(sql);
            try {
                query.executeUpdate();
            } catch (Exception e) { //org.postgresql.util.PSQLException
                //String getMsg = e.getMessage();
                // System.out.println("ERROR :"+getMsg);
                e.printStackTrace();
            }
        }
    }

    public void p(String str, String s) {
        System.out.println(str + "=" + s);
    }

    public void p(String str) {
        System.out.println(str);
    }

    public long doItDifferent() {
        long startTime = System.currentTimeMillis();
        EntityManager entityManager = JpaUtils.createEntityManager();
        try {
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();

            //drop tables only if they exist
            if (!getDPDTablesDrop().isEmpty()) {
                p("tables exist");
                insertLines(entityManager, getDPDTablesDrop());
            }
            insertLines(entityManager, getDPDTables());

            tx.commit();
            RecordParser recordParse = new RecordParser();
            try {
                ZipInputStream zipStream = getZipStream();
                ZipEntry ze = null;
                while ((ze = zipStream.getNextEntry()) != null) {
                    System.out.println("Files being open " + ze.getName());                    
                    recordParse.getDPDObject(ze.getName(),zipStream,entityManager);
                    //entityManager.flush();
                }


                zipStream = getInactiveZipStream() ;
                ze = null;
                while ((ze = zipStream.getNextEntry()) != null) {
                    System.out.println("Files being open " + ze.getName());
                    recordParse.getDPDObject(ze.getName(),zipStream,entityManager);
                    //entityManager.flush();
                }


                zipStream = getInactiveTableZipStream() ;
                ze = null;
                while ((ze = zipStream.getNextEntry()) != null) {
                    System.out.println("Files being open " + ze.getName());
                    recordParse.getDPDObject(ze.getName(),zipStream,entityManager);
                    //entityManager.flush();
                }

                p("populate interactions table with data");

                // Stream to read file
                    String url="/interactions-holbrook.txt";
                    InputStream ins=getClass().getResourceAsStream(url);
                    if (ins==null) System.out.println("ins is null");
                    recordParse.getDPDObject("interactions-holbrook.txt",ins,entityManager);

            } catch (Exception e) {
                e.printStackTrace();
            }




            tx.begin();

            //drop tables only if they exist
            if (!dropSearchTables().isEmpty()) {
                insertLines(entityManager, dropSearchTables());
            }


            insertLines(entityManager, getCreateSearchTables());

            tx.commit();

            ConfigureSearchData searchData = new ConfigureSearchData();
            //import search data
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
