package ru.home;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "proxy")
public class ProxyProperties {
    private Boolean migrationOn;
    private Upstream upstreamA = new Upstream();
    private Upstream upstreamB = new Upstream();
    private Integer weight;

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Boolean isMigrationOn() {
        return migrationOn;
    }

    public Boolean getMigrationOn() {
        return migrationOn;
    }

    public void setMigrationOn(Boolean migrationOn) {
        this.migrationOn = migrationOn;
    }

    public Upstream getUpstreamA() {
        return upstreamA;
    }

    public void setUpstreamA(Upstream upstreamA) {
        this.upstreamA = upstreamA;
    }

    public Upstream getUpstreamB() {
        return upstreamB;
    }

    public void setUpstreamB(Upstream upstreamB) {
        this.upstreamB = upstreamB;
    }

    public static class Upstream {

        private String uri;
        private int weight;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }
}

