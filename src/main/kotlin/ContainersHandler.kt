class ContainersHandler {

    private var containerSpecs: List<ContainerSpec>

    constructor(containerSpecs: List<ContainerSpec>) {

        this.containerSpecs = containerSpecs
    }

    fun getContainers(): List<ContainerSpec> {

        return containerSpecs
    }

    fun getContainerVolume(containerSpecs: ContainerSpec): Int {

        return DimensionsHelper().getDimensionsVolume(containerSpecs.dimensions)
    }

    fun getContainerTypeVolume(containerType: String): Int {

        val containerSpec = this.getContainers()
            .filter { containerSpec -> containerSpec.containerType === containerType }[0]

        return this.getContainerVolume(containerSpec)
    }
}