dependencies {
    implementation("com.netflix.graphql.dgs:graphql-dgs")
    implementation(project(":graphql-dgs-protocols-websockets"))

    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework:spring-websocket")
}