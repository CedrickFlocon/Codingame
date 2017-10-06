import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName

internal class MoveTest{

    lateinit var move : Move

    @Test
    internal fun move() {
        move = Move(Coordinate(100, 200), "BOOST")
        assertThat(move.move()).isEqualTo("100 200 BOOST")
    }

    @Test
    @DisplayName("Move when no thrust")
    internal fun moveWithoutThrust() {
        move = Move(Coordinate(100, 200), null)
        assertThatThrownBy { move.move() }.isInstanceOf(IllegalStateException::class.java)
    }
}