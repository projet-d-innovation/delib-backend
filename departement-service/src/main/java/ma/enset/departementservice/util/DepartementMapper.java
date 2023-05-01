package ma.enset.departementservice.util;


import feign.FeignException;
import ma.enset.departementservice.dto.*;
import ma.enset.departementservice.exception.ElementNotFoundException;
import ma.enset.departementservice.exception.InternalErrorException;
import ma.enset.departementservice.model.Departement;
import ma.enset.departementservice.proxy.UserFeignClient;
import ma.enset.departementservice.service.DepartementService;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface DepartementMapper {

    @Mapping(target = "filieresIds", expression = "java(new ArrayList<String>())")
    @Mapping(target = "nombreEmployes", expression = "java(1)")
    @Mapping(target = "usersIds", expression = "java(addChefToUsers(departementCreationRequest.codeChefDepartement(),departementService,userFeignClient))")
    Departement toDepartement(DepartementCreationRequest departementCreationRequest, DepartementService departementService, UserFeignClient userFeignClient);

    default List<String> addChefToUsers(String codeChefDepartement, DepartementService departementService, UserFeignClient userFeignClient) throws  InternalErrorException {
        if (departementService == null || userFeignClient == null) {
            throw new InternalErrorException();
        }
        //TODO: uncomment this
//        try{
//            if(!userFeignClient.doesUserExist(codeChefDepartement)){
//                throw  new  InternalErrorException();
//            }
//        }
//        catch (FeignException e){
//            throw new InternalErrorException();
//        }

        return List.of(codeChefDepartement);
    }
    DepartementResponse toDepartementResponse(Departement departement);

    List<DepartementResponse> toDepartementResponseList(List<Departement> departementlist);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDepartementFromDTO(DepartementUpdateRequest departementUpdateRequest, @MappingTarget Departement departement);

    @Mapping(target = "page", expression = "java(departementPage.getNumber())")
    @Mapping(target = "size", expression = "java(departementPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(departementPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(departementPage.getNumberOfElements())")
    @Mapping(source = "content", target = "records")
    DepartementPagingResponse toPagingResponse(Page<Departement> departementPage);



}
