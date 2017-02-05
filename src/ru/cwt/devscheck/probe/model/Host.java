package ru.cwt.devscheck.probe.model;

import ru.cwt.devscheck.probe.model.dict.AddressType;
import ru.cwt.devscheck.probe.model.dict.HostType;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author e.chertikhin
 * @date 12/01/2017
 *
 * Host - hostName definition
 *
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
public class Host {

    String name;
    HostType hostType;

    AddressType addressType;
    String address;

    HostGroup host;
    Set<ServiceGroup> groups;

    Set<ServiceCheck> checks;

    Date createDate;
    Date updateDate;
    Date lastCheckDate;

    public Host() {
    }

    public Host(String name, HostType hostType, String address, AddressType addressType) {
        this.name = name;
        this.hostType = hostType;
        this.address = address;
        this.addressType = addressType;
        this.groups = new HashSet<>();
        this.checks = new HashSet<>();
        this.createDate = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HostType getHostType() {
        return hostType;
    }

    public void setHostType(HostType hostType) {
        this.hostType = hostType;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public HostGroup getHost() {
        return host;
    }

    public void setHost(HostGroup host) {
        this.host = host;
    }

    public Set<ServiceGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<ServiceGroup> groups) {
        this.groups = groups;
    }

    public Set<ServiceCheck> getChecks() {
        return checks;
    }

    public void setChecks(Set<ServiceCheck> checks) {
        this.checks = checks;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getLastCheckDate() {
        return lastCheckDate;
    }

    public void setLastCheckDate(Date lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"name\"=\"").append(name).append("\"");
        sb.append(", \"hostType\"=\"").append(hostType).append("\"");
        sb.append(", \"addressType\"=\"").append(addressType).append("\"");
        sb.append(", \"address\"=\"").append(address).append("\"");
        sb.append(", \"hostName\"=\"").append(host).append("\"");
        sb.append(", \"groups\"=\"").append(groups).append("\"");
        sb.append(", \"checks\"=\"").append(checks).append("\"");
        sb.append(", \"createDate\"=\"").append(createDate).append("\"");
        sb.append(", \"updateDate\"=\"").append(updateDate).append("\"");
        sb.append(", \"lastCheckDate\"=\"").append(lastCheckDate).append("\"");
        sb.append('}');
        return sb.toString();
    }
}
