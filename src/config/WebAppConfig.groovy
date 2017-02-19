package config

import com.google.code.kaptcha.impl.DefaultKaptcha
import com.google.code.kaptcha.util.Config
import org.apache.commons.dbcp2.BasicDataSource
import org.hibernate.dialect.MySQL57InnoDBDialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.core.io.FileSystemResource
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.oxm.xstream.XStreamMarshaller
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.view.InternalResourceViewResolver
import org.springframework.web.servlet.view.json.MappingJackson2JsonView
import org.springframework.web.servlet.view.xml.MarshallingView
import util.ModelAndViewReturnValueHandler
import util.SuffixViewResolver

import javax.sql.DataSource


@Configuration
@ComponentScan(basePackages = ['repo','system','util','model','aspect','service'])
@EnableTransactionManagement
@EnableWebMvc
@EnableJpaRepositories(basePackages = ['repo','config','model'])
@EnableSpringDataWebSupport
@EnableAspectJAutoProxy
class WebAppConfig extends WebMvcConfigurerAdapter{

    static final File fileRootDir
    static final File fileTempDir

    static {
        if(System.getProperty('LOCAL')){
            fileRootDir=new File('D:/槐盟/file')
        }else{
            fileRootDir=new File('~/hm/file')
        }
        if(!fileRootDir.exists()) fileRootDir.mkdirs()
        fileTempDir=new File(fileRootDir,'temp')
        if(!fileTempDir.exists()) fileTempDir.mkdir()
        
    }

//  数据库及事务
    @Bean( name = 'dataSource')
    BasicDataSource getBasicDataSource(){
        def s = new BasicDataSource()
        s.driverClassName = 'com.mysql.cj.jdbc.Driver'
        s.url = 'jdbc:mysql://localhost:3306/hm?useSSL=false'
        s.username = 'root'
        s.password = 'jkjk1212'
        s
    }


    @Bean(name = 'entityManagerFactory')//配置EntityManagerFactory
    LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBean( DataSource s ){
        def fb = new LocalContainerEntityManagerFactoryBean()
        fb.packagesToScan = ['repo','model','config']


        def a = new HibernateJpaVendorAdapter()
        a.databasePlatform = MySQL57InnoDBDialect.class.name
        fb.jpaVendorAdapter = a
        fb.dataSource=s
        fb.jpaProperties = [
            'hibernate.dialect'     : 'org.hibernate.dialect.MySQL57InnoDBDialect',
            'hibernate.show_sql'    : 'true',
            'hibernate.format_sql'  : 'true',
            'hibernate.hbm2ddl.auto': 'update'
        ]
        fb
    }


    @Bean(name = 'transactionManager')
    JpaTransactionManager getJpaTransactionManager( LocalContainerEntityManagerFactoryBean f ){
        new JpaTransactionManager(f.object)
    }




//  视图、视图解析
    @Bean
    XStreamMarshaller getXStreamMarshaller(){ new XStreamMarshaller() }
    @Bean
    MappingJackson2JsonView getJsonView(){
        new MappingJackson2JsonView()
    }
    @Bean
    InternalResourceViewResolver getJspView(){ new InternalResourceViewResolver() }
    @Bean
    MarshallingView getXmlView( XStreamMarshaller xsm ){ new MarshallingView(xsm) }




//    Model返回值处理及视图后缀解析逻辑
    @Autowired
    ModelAndViewReturnValueHandler modelHandler
    @Override
    void addReturnValueHandlers( List<HandlerMethodReturnValueHandler> rvh ){ rvh.add(modelHandler) }

    @Autowired
    SuffixViewResolver resolver
    @Override
    void configureViewResolvers( ViewResolverRegistry registry ){
        resolver.order = 1
        registry.viewResolver(resolver)
    }




//    静态资源forward
    @Override
    void configureDefaultServletHandling( DefaultServletHandlerConfigurer configurer ){ configurer.enable() }

//文件上传
    @Bean(name = 'multipartResolver')
    CommonsMultipartResolver getCommonsMultipartResolver(){
        CommonsMultipartResolver r = new CommonsMultipartResolver()
//        r.fileItemFactory=new DiskFileItemFactory(1024*1024,new File(FileSystem.fileRootDir))
        r.defaultEncoding='UTF-8'
        r.maxUploadSizePerFile=100*1024*1024
        r.maxUploadSize=100*1024*1024
        r.uploadTempDir=new FileSystemResource(fileTempDir)
        r.preserveFilename=true
        r
    }
    
    
//    验证码
    @Bean
    DefaultKaptcha getDefaultKaptcha(){
        def kaptcha = new DefaultKaptcha()
        kaptcha.config=new Config(new Properties())
        kaptcha
    }

}