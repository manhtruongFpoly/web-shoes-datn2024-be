package com.example.demo.repository.impl;

import com.example.demo.common.DataUtil;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.payload.request.SearchDTO;
import com.example.demo.repository.ProductCustomRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final EntityManager entityManager;

    @Override
    public List<ProductDto> searchProductUser(SearchDTO<ProductDto> searchDTO){

        ProductDto productDto = searchDTO.getData();
//        StringBuilder sql = new StringBuilder("select \n" +
//                "    p.id,\n" +
//                "    p.brand_id,\n" +
//                "    p.category_id,\n" +
//                "    p.code,\n" +
//                "    p.create_date,\n" +
//                "    p.description,\n" +
//                "    p.discount,\n" +
//                "    p.name,\n" +
//                "    p.price,\n" +
//                "    p.price_new,\n" +
//                "    p.quantity,\n" +
//                "    p.update_date,\n" +
//                "    group_concat( DISTINCT s.name) as listSizes,\n" +
//                "    group_concat( DISTINCT c.name) as listColors\n" +
//                "from products p \n" +
//                "join product_sizes ps on p.id = ps.product_id\n" +
//                "join sizes s on s.id = ps.size_id\n" +
//                "join product_color pc on p.id = pc.product_id\n" +
//                "join clors c on c.id = pc.color_id\n" +
//                "where ((1 = 1 \n" +
//                "    AND ( :brandId is null or p.brand_id = :brandId)\n" +
//                "    AND ( :code is null or p.code = :code)\n" +
//                "    AND ( :categoryId is null or p.category_id = :categoryId)\n" +
//                "    AND ( :keySearch is null or p.name like CONCAT('%', :keySearch, '%') OR p.code like CONCAT('%', :keySearch, '%'))\n" +
//                "))\n" +
//                "group by p.id");

        StringBuilder sql = new StringBuilder("select \n" +
                "    p.id,\n" +
                "    p.brand_id,\n" +
                "    p.category_id,\n" +
                "    p.code,\n" +
                "    p.create_date,\n" +
                "    p.description,\n" +
                "    p.discount,\n" +
                "    p.name,\n" +
                "    p.price,\n" +
                "    p.price_new,\n" +
                "    p.quantity,\n" +
                "    p.update_date,\n" +
                "    CONCAT(\n" +
                "\t  '[',\n" +
                "\t\tGROUP_CONCAT(\n" +
                "\t\t\tDISTINCT CONCAT(\n" +
                "\t\t\t'{\"key\":', s.id, ', \"value\":\"', s.name, '\"}'\n" +
                "\t\t) ORDER BY s.id\n" +
                "\t\t),\n" +
                "\t']'\n" +
                ") AS listSizes,\n" +
                "    CONCAT(\n" +
                "\t  '[',\n" +
                "\t\tGROUP_CONCAT(\n" +
                "\t\t\tDISTINCT CONCAT(\n" +
                "\t\t\t'{\"key\":', c.id, ', \"value\":\"', c.name, '\"}'\n" +
                "\t\t) ORDER BY c.id\n" +
                "\t\t),\n" +
                "\t']'\n" +
                ") AS listColors,\n" +
                "    p.img_list,\n" +
                "    p.status\n" +
                "from products p \n" +
                "join product_sizes ps on p.id = ps.product_id\n" +
                "join sizes s on s.id = ps.size_id\n" +
                "join product_color pc on p.id = pc.product_id\n" +
                "join clors c on c.id = pc.color_id\n" +
                "where ((1 = 1 \n" +
                "    AND ( :brandId is null or p.brand_id = :brandId)\n" +
                "    AND ( :code is null or p.code = :code)\n" +
                "    AND ( :categoryId is null or p.category_id = :categoryId)\n" +
                "    AND ( :keySearch is null or p.name like CONCAT('%', :keySearch, '%') OR p.code like CONCAT('%', :keySearch, '%'))\n" +
                "))\n" +
                "group by p.id");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("code", productDto.getCode());
        query.setParameter("brandId", productDto.getBrandId());
        query.setParameter("categoryId", productDto.getCategoryId());
        query.setParameter("keySearch", productDto.getKeySearch());

        List<ProductDto> productDtoList = new ArrayList<>();
            List<Object[]> objects = query.getResultList();
        if (ObjectUtils.isNotEmpty(objects)) {
            for (Object[] obj : objects) {
                ProductDto productDto1 = new ProductDto();
                productDto1.setId(DataUtil.safeToLong(obj[0]));
                productDto1.setBrandId(DataUtil.safeToLong(obj[1]));
                productDto1.setCategoryId(DataUtil.safeToLong(obj[2]));
                productDto1.setCode(DataUtil.safeToString(obj[3]));
                productDto1.setCreateDate(DataUtil.safeToLocalDateTime(obj[4]));
                productDto1.setDescription(DataUtil.safeToString(obj[5]));
                productDto1.setDiscount(DataUtil.safeToInt(obj[6]));
                productDto1.setName(DataUtil.safeToString(obj[7]));
                productDto1.setPrice(DataUtil.safeToLong(obj[8]));
                productDto1.setPriceNew(DataUtil.safeToLong(obj[9]));
                productDto1.setQuantity(DataUtil.safeToInt(obj[10]));
                productDto1.setUpdateDate(DataUtil.safeToLocalDateTime(obj[11]));
                productDto1.setListSizes(DataUtil.safeToString(obj[12]));
                productDto1.setListColors(DataUtil.safeToString(obj[13]));
                productDto1.setImgList(DataUtil.safeToString(obj[14]));
                productDto1.setStatus(DataUtil.safeToInt(obj[15]));
                productDtoList.add(productDto1);
            }
        }
        return productDtoList;

    }

    @Override
    public ProductDto viewProductDetail(Long productId){
        StringBuilder sqlQuery = new StringBuilder();

        sqlQuery.append("select \n" +
                "    p.id,\n" +
                "    p.brand_id as brandId,\n" +
                "    p.category_id as categoryId,\n" +
                "    p.code,\n" +
                "    p.description,\n" +
                "    p.discount,\n" +
                "    p.name,\n" +
                "    p.price,\n" +
                "    p.price_new as priceNew,\n" +
                "    p.quantity,\n" +
                "    CONCAT(\n" +
                "\t  '[',\n" +
                "\t\tGROUP_CONCAT(\n" +
                "\t\t\tDISTINCT CONCAT(\n" +
                "\t\t\t'{\"key\":', s.id, ', \"value\":\"', s.name, '\"}'\n" +
                "\t\t) ORDER BY s.id\n" +
                "\t\t),\n" +
                "\t']'\n" +
                ") AS listSizes,\n" +
                "    CONCAT(\n" +
                "\t  '[',\n" +
                "\t\tGROUP_CONCAT(\n" +
                "\t\t\tDISTINCT CONCAT(\n" +
                "\t\t\t'{\"key\":', c.id, ', \"value\":\"', c.name, '\"}'\n" +
                "\t\t) ORDER BY c.id\n" +
                "\t\t),\n" +
                "\t']'\n" +
                ") AS listColors,\n" +
                "\t p.quantity, \n" +
                "\t p.img_list as imgList\n" +
                "from products p \n" +
                "join product_sizes ps on p.id = ps.product_id\n" +
                "join sizes s on s.id = ps.size_id\n" +
                "join product_color pc on p.id = pc.product_id\n" +
                "join clors c on c.id = pc.color_id\n" +
                "where p.id = :productId\n" +
                "group by p.id");

        NativeQuery<ProductDto> query = ((Session) entityManager.getDelegate()).createNativeQuery(sqlQuery.toString());
        query
                .addScalar("id", new LongType())
                .addScalar("brandId", new LongType())
                .addScalar("categoryId", new LongType())
                .addScalar("code", new StringType())
                .addScalar("description", new StringType())
                .addScalar("discount", new IntegerType())
                .addScalar("name", new StringType())
                .addScalar("price", new LongType())
                .addScalar("priceNew", new LongType())
                .addScalar("listSizes", new StringType())
                .addScalar("listColors", new StringType())
                .addScalar("quantity", new IntegerType())
                .addScalar("imgList", new StringType())
                .setResultTransformer(Transformers.aliasToBean(ProductDto.class));


        if (productId != null) {
            query.setParameter("productId", productId);
        }

        return query.uniqueResult();
    }



}
