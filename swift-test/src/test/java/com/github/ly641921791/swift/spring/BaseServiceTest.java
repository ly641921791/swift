package com.github.ly641921791.swift.spring;

import com.github.ly641921791.swift.ExceptionAssert;
import com.github.ly641921791.swift.core.mapper.param.Condition;
import com.github.ly641921791.swift.test.FooApplication;
import com.github.ly641921791.swift.test.service.FooService;
import com.github.ly641921791.swift.test.service.FooWithAnnotationService;
import com.github.ly641921791.swift.test.table.Foo;
import com.github.ly641921791.swift.test.table.FooWithAnnotation;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author ly
 * @since 1.0.0
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FooApplication.class)
public class BaseServiceTest {

    @Autowired
    private FooService fooService;

    @Autowired
    private FooWithAnnotationService fooWithAnnotationService;

    @Test
    public void countTestSuccess() {
        Assert.assertTrue(fooService.count() > fooWithAnnotationService.count());

        Condition condition = new Condition();
        condition.eq(Foo.ID, 1L);
        condition.orderByDesc(Foo.ID);
        condition.limit(1);
        fooService.count(condition);
        fooWithAnnotationService.count(condition);
    }

    @Test
    public void deleteTestSuccess() {
        Condition condition = new Condition();
        condition.eq(Foo.ID, 1L);
        condition.eq(Foo.STRING_VALUE, "foo");
        fooService.delete(condition);

        condition = new Condition();
        condition.eq(FooWithAnnotation.ID, 1L);
        condition.eq(FooWithAnnotation.STRING_VALUE, "foo");
        fooWithAnnotationService.delete(condition);
    }

    @Test
    public void deleteByColumnTestSuccess() {
        Assert.assertEquals(fooService.deleteByColumn(Foo.ID, 11L), 1);
        Assert.assertEquals(fooService.deleteByColumn(Foo.ID, 11L), 0);

        Assert.assertEquals(fooWithAnnotationService.deleteByColumn(Foo.ID, 11L), 1);
        Assert.assertEquals(fooWithAnnotationService.deleteByColumn(Foo.ID, 11L), 0);
    }

    @Test
    public void deleteByIdTestSuccess() {
        fooService.deleteById(2L);
        Assert.assertNull(fooService.findById(2L));

        fooWithAnnotationService.deleteById(2L);
        Assert.assertNull(fooWithAnnotationService.findById(2L));
    }

    @Test
    public void findAllTestSuccess() {
        Assert.assertTrue(fooService.findAll().size() > fooWithAnnotationService.findAll().size());

        Condition condition = new Condition();
        condition.eq(Foo.ID, 1L);
        condition.like(Foo.STRING_VALUE, "like");
        condition.orderByDesc(Foo.ID);
        condition.limit(1);
        fooService.findAll(condition);

        condition = new Condition();
        condition.eq(FooWithAnnotation.ID, 1L);
        condition.like(FooWithAnnotation.STRING_VALUE, "like");
        condition.orderByDesc(FooWithAnnotation.ID);
        condition.limit(1);
        fooWithAnnotationService.findAll(condition);
    }

    @Test
    public void findAllIdTestSuccess() {
        Condition condition = new Condition();
        condition.eq(Foo.STRING_VALUE, "findAllId");
        Assert.assertTrue(fooService.findAllId().size() > 0);
        Assert.assertTrue(fooService.findAllId(condition).size() > 0);

        condition = new Condition();
        condition.eq(FooWithAnnotation.STRING_VALUE, "findAllId");
        Assert.assertTrue(fooWithAnnotationService.findAllId().size() > 0);
        Assert.assertTrue(fooWithAnnotationService.findAllId(condition).size() > 0);
    }

    @Test
    public void findAllByColumnTestSuccess() {
        Assert.assertEquals(fooService.findAllByColumn(Foo.STRING_VALUE, "findAllByColumn").size(), 2);
        Assert.assertEquals(fooService.findAllByColumn(Foo.STRING_VALUE, Collections.singleton("findAllByColumn")).size(), 2);
        Assert.assertEquals(fooService.findAllByColumn(Foo.STRING_VALUE, Collections.singletonList("findAllByColumn")).size(), 2);
        Assert.assertEquals(fooWithAnnotationService.findAllByColumn(FooWithAnnotation.STRING_VALUE, "findAllByColumn").size(), 1);
        Assert.assertEquals(fooWithAnnotationService.findAllByColumn(FooWithAnnotation.STRING_VALUE, Collections.singleton("findAllByColumn")).size(), 1);
        Assert.assertEquals(fooWithAnnotationService.findAllByColumn(FooWithAnnotation.STRING_VALUE, Collections.singletonList("findAllByColumn")).size(), 1);
    }

    @Test
    public void findAllByIdTestSuccess() {
        Assert.assertEquals(fooService.findAllById(Arrays.asList(8L, 9L)).size(), 2);
        Assert.assertEquals(fooWithAnnotationService.findAllById(Arrays.asList(8L, 9L)).size(), 1);
    }

    @Test
    public void findByIdTestSuccess() {
        Assert.assertNotNull(fooService.findById(1L));
        Assert.assertNotNull(fooWithAnnotationService.findById(1L));
    }

    @Test
    public void findOneByColumnTestSuccess() {
        fooService.findOneByColumn(Foo.STRING_VALUE, "findOneByColumn");
        fooWithAnnotationService.findOneByColumn(FooWithAnnotation.STRING_VALUE, "findOneByColumn");
    }

    @Test
    public void saveTestSuccess() {
        Foo foo = new Foo();
        foo.setId(new Random().nextLong());
        foo.setDel(0);

        Assert.assertEquals(fooService.save(foo), 1);
        ExceptionAssert.assertException(DuplicateKeyException.class, () -> fooService.save(foo));
        Assert.assertEquals(fooService.save(foo, true), 0);

        FooWithAnnotation fooWithAnnotation = new FooWithAnnotation();
        fooWithAnnotation.setId(new Random().nextLong());
        fooWithAnnotation.setDel(0);

        Assert.assertEquals(fooWithAnnotationService.save(fooWithAnnotation), 1);
        ExceptionAssert.assertException(DuplicateKeyException.class, () -> fooWithAnnotationService.save(fooWithAnnotation));
        Assert.assertEquals(fooWithAnnotationService.save(fooWithAnnotation, true), 0);
    }

    @Test
    public void saveAllTestSuccess() {
        List<Long> idList = Arrays.asList(1001L, 1002L);
        final List<Foo> fooList = new ArrayList<>();
        idList.forEach(id -> {
            Foo foo = new Foo();
            foo.setId(id);
            foo.setDel(0);
            fooList.add(foo);
        });

        fooService.saveAll(fooList);

        List<Foo> fooNewList = fooService.findAllById(idList);

        Assert.assertEquals(fooNewList.size(), fooList.size());
    }

    @Test
    public void updateByColumnTestSuccess() {
        Foo fooTargetProperty = new Foo();
        fooTargetProperty.setStringValue("updateSuccess");
        FooWithAnnotation foo2targetProperty = new FooWithAnnotation();
        foo2targetProperty.setStringValue("updateSuccess");

        fooService.updateByColumn(fooTargetProperty, Foo.ID, 3L);
        Assert.assertEquals(fooService.findById(3L).getStringValue(), fooTargetProperty.getStringValue());

        fooWithAnnotationService.updateByColumn(foo2targetProperty, Foo.ID, 3L);
        Assert.assertEquals(fooWithAnnotationService.findById(3L).getStringValue(), foo2targetProperty.getStringValue());
    }

    @Test
    public void updateByIdTestSuccess() {
        Foo fooTargetProperty = new Foo();
        fooTargetProperty.setStringValue("updateSuccess");
        FooWithAnnotation foo2targetProperty = new FooWithAnnotation();
        foo2targetProperty.setStringValue("updateSuccess");


        fooService.updateById(fooTargetProperty, 3L);
        Assert.assertEquals(fooService.findById(3L).getStringValue(), fooTargetProperty.getStringValue());

        fooWithAnnotationService.updateById(foo2targetProperty, 3L);
        Assert.assertEquals(fooWithAnnotationService.findById(3L).getStringValue(), foo2targetProperty.getStringValue());
    }

    @Test
    public void updateColumnByIdTestSuccess() {
        fooService.updateColumnById(Foo.STRING_VALUE, "111", 1L);
        fooWithAnnotationService.updateColumnById(FooWithAnnotation.STRING_VALUE, "111", 1L);
    }

}
