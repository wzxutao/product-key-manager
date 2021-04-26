package cn.rypacker.productkeymanager.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MandatoryFieldsManagerImplTest {

    MandatoryFieldsManager m = new MandatoryFieldsManagerImpl();

    @Test
    void add_remove_read() {
        m.clear();
        var n1 = "名字";
        var n2 = "日期";
        var n3 = "your mom";
        var n4 = "なにこれ";
        m.addField(n1);
        var rv = m.getFieldNames();
        assertEquals(rv.size(), 1);
        assertTrue(rv.contains(n1));

        m.addField(n2);
        rv = m.getFieldNames();
        assertEquals(rv.size(), 2);
        assertTrue(rv.contains(n2));
        assertTrue(rv.contains(n1));

        m.addField(n3);
        m.addField(n4);
        rv = m.getFieldNames();
        assertEquals(rv.size(), 4);
        assertTrue(rv.contains(n1));
        assertTrue(rv.contains(n2));
        assertTrue(rv.contains(n3));
        assertTrue(rv.contains(n4));

        m.removeField(n2);
        rv = m.getFieldNames();
        assertEquals(rv.size(), 3);
        assertFalse(rv.contains(n2));

        // clear
        m.clear();
        assertEquals(m.getFieldNames().size(), 0);

        // no duplication
        m.addField(n1);
        assertEquals(m.getFieldNames().size(), 1);
        m.addField(n1);
        assertEquals(m.getFieldNames().size(), 1);
        m.addField(n2);
        m.addField(n3);
        assertEquals(m.getFieldNames().size(), 3);
        m.addField(n2);
        m.addField(n2);
        m.addField(n2);
        m.addField(n1);
        assertEquals(m.getFieldNames().size(), 3);
        m.clear();
        assertEquals(m.getFieldNames().size(), 0);



        // edge cases
        m.addField(null);
        assertEquals(m.getFieldNames().size(), 0);

        m.addField("");
        assertEquals(m.getFieldNames().size(), 0);

        m.addField("               ");
        assertEquals(m.getFieldNames().size(), 0);

        m.addField("\n");
        assertEquals(m.getFieldNames().size(), 0);

    }

}