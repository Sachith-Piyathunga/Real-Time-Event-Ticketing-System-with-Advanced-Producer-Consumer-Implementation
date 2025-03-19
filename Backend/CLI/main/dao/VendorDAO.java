package main.dao;

import main.models.Vendor;
import java.util.List;


public interface VendorDAO  //Interface for performing data operations related to Vendors.
{
    boolean addVendor(Vendor vendor);
    boolean updateVendor(Vendor vendor);
    boolean deleteVendor(int vendorId);
    Vendor getVendor(int vendorId);
    List<Vendor> getAllVendors();
    boolean existsVendor(int vendorId);
    int getVendorCount();
}
