package networking.requesthandlers;


// This interface serves little purpose other than ensuring consistency across different specific SocketHandlers.
// Consistency and uniformitet is nice.
public interface RequestHandler
{
    Object handle(String action, Object payload);
}
