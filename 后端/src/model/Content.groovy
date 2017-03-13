package model

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

import javax.persistence.*

@Entity
@JsonIgnoreProperties( ['hibernateLazyInitializer','handler'] )
class Content{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @JsonIgnore
    Long id

    @Column(length=10000)
    String text

    @OneToMany(fetch=FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    List<ResourceFile> files
}


@Entity
@JsonIgnoreProperties( ['hibernateLazyInitializer','handler'] )
class ResourceFile{


    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    Long    id

    String  name

    Long    size

    ResourceFile(File f){
        name=f.name
        size=f.size()
    }
    ResourceFile(){}

}